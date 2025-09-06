const yInput = document.getElementById('y')
const xCheckbox = document.querySelectorAll('#choice_of_x input[type="checkbox"]')
const rChoice = document.querySelectorAll('#choice_of_r input[type="radio"]')
const submitButton = document.getElementById('submit_button')
const clearFormButton = document.getElementById('clear_form_button')
const clearTableButton = document.getElementById('clear_table_button')
const tableWithResults = document.getElementById("result_table")
let lastCheckedX = null
let lastSelectedR = null
let currentR = null;

redraw()

window.onload = function () {
    let history = JSON.parse(localStorage.getItem('results') || '[]')
    history.forEach(addNewRow)
}

function sendRequest (checkedX, enteredY, selectedR) {
    const dataForRequest = {
        x: checkedX,
        y: enteredY,
        r: selectedR
    }
    $.ajax({
        url: "/api?" + $.param(dataForRequest),
        type: "GET",
        dataType: "json",
        success: function (response) {
            if (!response.error) {
                let data = {
                    x: dataForRequest.x,
                    y: dataForRequest.y,
                    r: dataForRequest.r,
                    hit: response.hit,
                    serverTime: response.serverTime,
                    scriptTime: response.scriptTime
                }
                addNewRow(data)
                saveResultToLocalStorage(data)
                const ctx = initCanvas()
                drawPointOnCoordinatePlane(ctx, parseInt(dataForRequest.x), parseFloat(dataForRequest.y), response.hit)
            }
        },
        error: function (jqXHR, textStatus, errorThrown) {
            showMessage(errorThrown || textStatus)
        }
    })
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
    if (message) {
        errorField.hidden = false
        errorField.textContent = message
    } else {
        errorField.hidden = true
        errorField.textContent = ""
    }
}

function submit() {
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
        sendRequest(x, y, r);
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
    if (y === "") {
        showMessage("Необходимо ввести координату Y!");
        return false;
    }
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


