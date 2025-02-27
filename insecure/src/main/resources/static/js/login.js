(function () {

const loginUrl = "http://localhost:8080/api/login";
const successPage = "/html/comment.html";

document.getElementById("loginForm").onsubmit = async function() {
    event.preventDefault();
    let email = document.getElementById("exampleInputEmail1").value;
    let password = document.getElementById("exampleInputPassword1").value;
    await login(email, password)
}

async function login(username, password) {
    try {
        let response = await fetch(loginUrl, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify({
                    username: username,
                    password: password
                })
            });
        const data = await response.json();
        console.log(data)
        if (response.ok) {
            window.location.href = successPage;
        } else {
            document.getElementById("errorMessage").innerHTML = ("Invalid login credential")
        }
    } catch (err) {
        document.getElementById("errorMessage").innerHTML = ("Error: " + err.message + " Please try again later")
    }

}
})();
