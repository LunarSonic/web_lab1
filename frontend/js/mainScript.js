import {initCanvas, drawPointOnCoordinatePlane, redraw} from "./canvas.js";
const yInput = document.getElementById('y')
const xCheckbox = document.querySelectorAll('#choice_of_x input[type="checkbox"]')
const rChoice = document.querySelectorAll('#choice_of_r input[type="radio"]')
const mainForm = document.getElementById("main_form")
const clearFormButton = document.getElementById('clear_form_button')
const clearTableButton = document.getElementById('clear_table_button')
const tableWithResults = document.getElementById("result_table")
const floatPattern = /^-?(?:0|[1-9][0-9]*)(?:\.[0-9]+)?$/
let lastCheckedX = null
let lastSelectedR = null
let currentR = null

window.onload = function () {
    redraw()
    let history = JSON.parse(localStorage.getItem('results') || '[]')
    history.forEach(addNewRow)
}

async function sendRequest (checkedX, enteredY, selectedR) {
    const dataForRequest = {
        x: checkedX,
        y: enteredY,
        r: selectedR
    }
    try {
        const parameters = new URLSearchParams(dataForRequest).toString()
        const response = await fetch(`/api?${parameters}`)
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
            saveResultToLocalStorage(data)
            const ctx = initCanvas()
            drawPointOnCoordinatePlane(ctx, parseInt(result.x), parseFloat(result.y), result.hit)
        } else {
            const errorField = document.getElementById('error')
            if (errorField) {
                showMessage(errorField, `Ошибка при отправке запроса: ${response.status}`)
            }
        }
    } catch (error) {
        const errorField = document.getElementById('error')
        if (errorField) {
            showMessage(errorField, `Ошибка: ${error.message}`)
        }
    }
}


function addNewRow(data) {
    const tbody = tableWithResults.querySelector('tbody')
    let row = tbody.insertRow(-1)
    row.insertCell(0).textContent = data.x
    row.insertCell(1).textContent = data.y
    row.insertCell(2).textContent = data.r
    row.insertCell(3).textContent = data.hit ? 'Да' : 'Нет'
    row.insertCell(4).textContent = data.serverTime
    row.insertCell(5).textContent = data.scriptTime
}

function saveResultToLocalStorage(data) {
    let savedResult = JSON.parse(localStorage.getItem('results') || '[]')
    savedResult.push(data)
    localStorage.setItem('results', JSON.stringify(savedResult))
}

function chooseOnlyOneCheckbox(event) {
    const clickedCheckbox = event.target
    if (clickedCheckbox.checked) {
        // если выбрали новый чекбокс
        if (lastCheckedX !== clickedCheckbox && lastCheckedX) {
            lastCheckedX.checked = false
        }
        lastCheckedX = clickedCheckbox
    }
    else {
        if (lastCheckedX === clickedCheckbox) {
            lastCheckedX = null
        }
    }
}
xCheckbox.forEach(x => {
    x.addEventListener('change', chooseOnlyOneCheckbox)
})

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
    const checkedX = document.querySelector('#choice_of_x input[type="checkbox"]:checked')
    const selectedR = document.querySelector('#choice_of_r input[type="radio"]:checked')
    const isXValid = validateX()
    const isYValid = validateY(y)
    const isRValid = validateR()
    if (isXValid && isYValid && isRValid) {
        const x = checkedX.value
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


function clearTable() {
    localStorage.removeItem('results')
    const tbody = document.getElementById('body_for_table')
    tbody.innerHTML = ''
}
clearTableButton.addEventListener('click', clearTable)

function handleRadioChange(event) {
    const currentRadio = event.target
    if (lastSelectedR && lastSelectedR !== currentRadio) {
        lastSelectedR.checked = false
    }
    lastSelectedR = currentRadio.checked ? currentRadio : null
    currentR = parseFloat(currentRadio.value)
    redraw(currentR)
}
rChoice.forEach(radio => {
    radio.addEventListener('change', handleRadioChange)
})


const validateX = function() {
    const checkedX = document.querySelectorAll('input[type="checkbox"]:checked')
    const xErrorField = document.getElementById("errorX")
    if (checkedX.length === 0) {
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
        return false;
    } else {
        showMessage(rErrorField, "")
        return true
    }
}