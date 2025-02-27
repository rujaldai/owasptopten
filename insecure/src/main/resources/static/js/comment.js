// (function () {

// const postCommentUrl = "http://localhost:8080/api/users/comments";

// function evalOrSwallow(script) {
//     try {
//         return eval(script);
//     } catch (err) {
//         console.error(err);
//         return script;
//     }
// }

// function getComments() {
//     fetch(postCommentUrl + "?page=0&size=10")
//         .then(response => response.json())
//         .then(data => {
//             data.forEach(comment => {
//                 const listGroupItem = document.createElement("li");
//                 listGroupItem.className = "list-group-item";
//                 const scriptEL = document.createRange().createContextualFragment(comment.comment);

//                 listGroupItem.append(comment.username + " - ")
//                 listGroupItem.append(scriptEL);

//                 document.getElementById("comments").append(listGroupItem);
//             });
//         });
// }

// document.getElementById("commentForm").onsubmit = async function() {
//     event.preventDefault();
//     let comment = document.getElementById("comment").value;
//     await postComment(comment)
// }

// async function postComment(comment) {
//     try {
//         let response = await fetch(postCommentUrl, {
//                 method: "POST",
//                 headers: {
//                     "Content-Type": "application/json"
//                 },
//                 body: JSON.stringify({
//                     comment: comment
//                 })
//             });
// //        const data = await response.json();
// //        console.log(data)
//         if (response.ok) {
//             document.getElementById("comments").innerHTML += (`<li class="list-group-item">${comment}</li>`)

// //            data.forEach(comment => {
// //                document.getElementById("comments").innerHTML += (`<li class="list-group-item">${comment.username} - ${comment.comment}</li>`)
// //            });
//         } else {
//             document.getElementById("errorMessage").innerHTML = ("Failed")
//         }
//     } catch (err) {
//         document.getElementById("errorMessage").innerHTML = ("Error: " + err.message + " Please try again later")
//     }

// }

// getComments();
// })();
