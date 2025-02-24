
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