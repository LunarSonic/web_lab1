const yInput = document.getElementById('y')
const xCheckbox = document.querySelectorAll('#choice_of_x input[type="checkbox"]')
const rChoice = document.querySelectorAll('#choice_of_r input[type="radio"]')
const submitButton = document.getElementById('submit_button')
const clearFormButton = document.getElementById('clear_form_button')
const clearTableButton = document.getElementById('clear_table_button')
const tableWithResults = document.getElementById("result_table")
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
        }
    } catch (error) {
        showMessage(error.message)
    }
}

function addNewRow(data) {
    const tbody = tableWithResults.querySelector('tbody')
    let row = tbody.insertRow(0)
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

function showMessage(message) {
    const errorField = document.getElementById('error')
    errorField.onanimationend = null
    if (message) {
        errorField.hidden = false
        errorField.style.animation = 'fadeInAndFadeOut 3s'
        errorField.textContent = message
        errorField.onanimationend = () => {
            errorField.hidden = true
            errorField.textContent = ""
        }
    } else {
        errorField.hidden = true
        errorField.style.animation = 'none'
        errorField.textContent = ""
    }
}

async function submit() {
    const y = yInput.value.trim()
    const checkedX = document.querySelector('#choice_of_x input[type="checkbox"]:checked')
    const selectedR = document.querySelector('#choice_of_r input[type="radio"]:checked')
    if (!checkedX && y === "" && !selectedR) {
        showMessage("Необходимо ввести X, Y, R!")
    }
    const isXValid = validateX();
    const isYValid = validateY(y);
    const isRValid = validateR();
    if (isXValid && isYValid && isRValid) {
        const x = checkedX.value;
        const r = selectedR.value;
        await sendRequest(x, y, r);
    }
}
submitButton.addEventListener('click', submit)

function clearForm() {
    xCheckbox.forEach(x => x.checked = false)
    yInput.value = ''
    rChoice.forEach(r => r.checked = false)
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
    if (checkedX.length === 0) {
        showMessage("Необходимо выбрать координату X!");
        return false;
    }
    return true
}

const validateY = function(y) {
    const yFloat = parseFloat(y);
    if (isNaN(yFloat)) {
        showMessage("Координата Y должна быть числом!");
        return false;
    }
    if (yFloat < -3 || yFloat > 3) {
        showMessage("Координата Y должна принимать значение от -3 до 3!");
        return false;
    }
    return true;
}


const validateR = function() {
    const selectedR = document.querySelector('input[type="radio"]:checked')
    if (!selectedR) {
        showMessage("Необходимо выбрать координату R!");
        return false;
    }
    return true
}


