async function createOwner(){
    clearFormErrors()

    let name = document.getElementById("ownerName").value;
    let email = document.getElementById("ownerEmail").value;
    let password = document.getElementById("password").value;
    let repeatedPassword = document.getElementById("repeatedPassword").value;
    let csrfToken = document.querySelector('meta[name="_csrf"]').content;
    let csrfHeader = document.querySelector('meta[name="_csrf_header"]').content;

    let body = {
        name : name,
        password : password,
        repeatedPassword : repeatedPassword,
        email : email
    }
    let headers = new Headers();
    headers.append(`${csrfHeader}`,csrfToken);
    headers.append("Content-Type","application/json");
    try{
        const response = await fetch("owner",{
            method : "POST",
            body: JSON.stringify(body),
            headers : headers
        });

        if(response.status == 400){
            return badRequestHandler(await response.json())
        }

        document.getElementById("success").appendChild(createSuccessElement(await response.text()));
    }catch(error){
        console.log(error.message);
    }
}

function badRequestHandler(errors){
    for(error of errors){
        if(!error.hasOwnProperty("fieldName")){
            document.getElementById("errors").appendChild(createErrorElement(error));
        }else{
            let invalidFeedbackElement;
            let invalidInputElement;
            switch(error.fieldName){
                case "name" :
                    invalidFeedbackElement = document.getElementById("nameInvalidFeedback");
                    invalidInputElement = document.getElementById("ownerName");
                    break;
                case "email" :
                    invalidFeedbackElement = document.getElementById("emailInvalidFeedback");
                    invalidInputElement = document.getElementById("ownerEmail");
                    break ;
                case "password":
                    invalidFeedbackElement = document.getElementById("passwordInvalidFeedback");
                    invalidInputElement = document.getElementById("password");
                    break;
            }
            invalidInputElement.classList.add("is-invalid");
            invalidFeedbackElement.innerHTML += `${error.message}\n`;
        }
    }
}

function createErrorElement(error){
    const alertDiv = document.createElement("div");
    const textDiv = document.createElement("div");
    const closeBtn = document.createElement("button");

    alertDiv.className = "alert alert-danger alert-dismissible fade show mt-1 mb-1";
    closeBtn.className = "btn-close";
    closeBtn.setAttribute("data-bs-dismiss","alert");
    closeBtn.setAttribute("aria-label","Close");
    textDiv.textContent = error.message;

    alertDiv.appendChild(textDiv);
    alertDiv.appendChild(closeBtn);

    return alertDiv;
}

function createSuccessElement(msg){
    const alertDiv = document.createElement("div");
    const textDiv = document.createElement("div");
    const closeBtn = document.createElement("button");

    alertDiv.className = "alert alert-success alert-dismissible fade show mt-1 mb-1";
    closeBtn.className = "btn-close";
    closeBtn.setAttribute("data-bs-dismiss","alert");
    closeBtn.setAttribute("aria-label","Close");
    textDiv.textContent = msg;

    alertDiv.appendChild(textDiv);
    alertDiv.appendChild(closeBtn);

    return alertDiv;
}

function clearFormErrors(){
    document.getElementById("errors").innerHTML = '';
    document.getElementById("nameInvalidFeedback").innerHTML = '';
    document.getElementById("emailInvalidFeedback").innerHTML = '';
    document.getElementById("passwordInvalidFeedback").innerHTML = '';

    document.getElementById("ownerName").classList.remove("is-invalid");
    document.getElementById("ownerEmail").classList.remove("is-invalid");
    document.getElementById("password").classList.remove("is-invalid");
}

function clearFormInputs(){
    document.getElementById("ownerName").value = "";
    document.getElementById("ownerEmail").value = "";
    document.getElementById("password").value = "";
}