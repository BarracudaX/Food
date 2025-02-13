window.onload = function(){
    const urlParams = new URLSearchParams(window.location.search);
    const tokenInput = document.getElementById("ott_token");
    const ottToken = urlParams.get('token');

    tokenInput.value= ottToken;

    document.getElementById("ottForm").submit();
}