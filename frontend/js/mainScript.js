import {initCanvas, drawPointOnCoordinatePlane, redraw} from "./canvas.js"
const yInput = document.getElementById('y')
const xSelect = document.getElementById('x')
const rChoice = document.querySelectorAll('#choice_of_r input[type="radio"]')
const errorField = document.getElementById('error')
const mainForm = document.getElementById("main_form")
const clearFormButton = document.getElementById('clear_form_button')
const clearTableButton = document.getElementById('clear_table_button')
const tableWithResults = document.getElementById("result_table")
const floatPattern = /^-?(?:0|[1-9][0-9]*)(?:\.[0-9]+)?$/

window.onload = async function () {
    const history = await loadHistory()
    if (history) {
        updateTable(history)
        updateCanvas(history)
    }
}

async function loadHistory() {
    try {
        const response = await fetch('/api/history')
        if (response.ok) {
            return await response.json()
        } else {
            showMessage(errorField, `Ошибка при загрузке истории: ${response.status}`)
        }
    } catch (error) {
        showMessage(errorField, `Ошибка: ${error.message}`)
    }
    return []
}

function updateTable(history) {
    const tbody = tableWithResults.querySelector('tbody')
    tbody.innerHTML = ''
    history.forEach(addNewRow)
}

function updateCanvas(history, r = null) {
    redraw(r)
    const ctx = initCanvas()
    history.forEach(data => {
        drawPointOnCoordinatePlane(ctx, parseInt(data.x, 10), parseFloat(data.y), data.hit)
    })
}

async function sendRequest (selectedX, enteredY, selectedR) {
    const dataForRequest = {
        x: selectedX,
        y: enteredY,
        r: selectedR
    }
    try {
        const parameters = new URLSearchParams(dataForRequest).toString()
        const response = await fetch(`/api/points?${parameters}`)
        if (response.ok) {
            const result = await response.json()
            let data = {
                x: result.x,
                y: result.y,
                r: result.r,
                hit: result.hit,
                serverTime: result.serverTime,
                scriptTime: result.scriptTime
            }
            addNewRow(data)
            const history = await loadHistory()
            updateCanvas(history, parseFloat(selectedR))
        } else {
            if (errorField) {
                showMessage(errorField, `Ошибка при отправке запроса: ${response.status}`)
            }
        }
    } catch (error) {
        if (errorField) {
            showMessage(errorField, `Ошибка: ${error.message}`)
        }
    }
}


function addNewRow(data) {
    const { x, y, r, hit, serverTime, scriptTime } = data
    const tbody = tableWithResults.querySelector('tbody')
    let row = tbody.insertRow(-1)
    row.insertCell(0).textContent = x
    row.insertCell(1).textContent = y
    row.insertCell(2).textContent = r
    row.insertCell(3).textContent = hit ? 'Да' : 'Нет'
    row.insertCell(4).textContent = serverTime
    row.insertCell(5).textContent = scriptTime
}


function showMessage(element, message) {
    element.onanimationend = null
    if (message) {
        element.hidden = false
        element.style.animation = 'fadeInAndFadeOut 3s'
        element.textContent = message
        element.onanimationend = () => {
            element.hidden = true
            element.textContent = ""
        }
    } else {
        element.hidden = true
        element.style.animation = 'none'
        element.textContent = ""
    }
}

async function handleFormSubmission(event) {
    event.preventDefault()
    const y = yInput.value.trim()
    const selectedR = document.querySelector('#choice_of_r input[type="radio"]:checked')
    const isXValid = validateX()
    const isYValid = validateY(y)
    const isRValid = validateR()
    if (isXValid && isYValid && isRValid) {
        const x = xSelect.value
        const r = selectedR.value
        await sendRequest(x, y, r)
    }
}
mainForm.addEventListener('submit', handleFormSubmission)


function clearForm() {
    mainForm.reset()
    redraw()
}
clearFormButton.addEventListener('click', clearForm)


async function clearTable() {
    try {
        const response = await fetch('/api/clear', {
            method: 'POST'
        })
        if (response.ok) {
            const tbody = document.getElementById('body_for_table')
            tbody.innerHTML = ''
            redraw()
        } else {
            if (errorField) {
                showMessage(errorField, `Ошибка при очистке истории: ${response.status}`)
            }
        }
    } catch (error) {
        if (errorField) {
            showMessage(errorField, `Ошибка: ${error.message}`)
        }
    }
}
clearTableButton.addEventListener('click', clearTable)


async function handleRadioChange(event) {
    const currentRadio = event.target
    const currentR = parseFloat(currentRadio.value)
    const history = await loadHistory()
    updateCanvas(history, currentR)
}
rChoice.forEach(radio => {
    radio.addEventListener('change', handleRadioChange)
})


const validateX = function() {
    const x = xSelect.value
    const xErrorField = document.getElementById("errorX")
    if (x === "") {
        showMessage(xErrorField, "Необходимо выбрать координату X!")
        return false
    } else {
        showMessage(xErrorField, "")
        return true
    }
}


const validateY = function(y) {
    const yErrorField = document.getElementById("errorY")
    if (y === '') {
        showMessage(yErrorField, "Не введена координата Y!")
        return false
    } else if (!floatPattern.test(y)) {
        showMessage(yErrorField, "Координата Y должна быть числом!")
        return false
    }
    const yFloat = parseFloat(y)
    if (yFloat < -3 || yFloat > 3) {
        showMessage(yErrorField, "Координата Y должна принимать значение от -3 до 3!")
        return false
    } else {
        showMessage(yErrorField, "")
        return true
    }
}


const validateR = function() {
    const rErrorField = document.getElementById("errorR")
    const selectedR = document.querySelector('input[type="radio"]:checked')
    if (!selectedR) {
        showMessage(rErrorField, "Необходимо выбрать координату R!")
        return false
    } else {
        showMessage(rErrorField, "")
        return true
    }
}