import axios from "axios";


export async function getAll() {
    // try {
    const result = await axios('/date/');
    console.log(result);
    return result;
    // } catch (err) {
    //     StaticServices.handleError(err);
    //     return []; //in case we have a problem getting the applications
    // }
}

export async function login(user) {
    console.log("in login");
    const result = await fetch("/server_Web_exploded/login", {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        redirect: 'follow',
        referrerPolicy: 'no-referrer',
        body: JSON.stringify(user)
    });
    console.log(result);
    return result.data;
}

export async function addNew(username) {
    const body = JSON.stringify({"username":username});
    const result = await fetch("/server_Web_exploded/login", {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        redirect: 'follow',
        referrerPolicy: 'no-referrer',
        body: body
    });
    let responseBody;
    if(result.headers.get("content-type").includes("application/json")){
        responseBody = await result.json();
    }
    else{
        responseBody = await result.text();
    }
    if (!result.ok){
        throw  new Error("error") //todo create an exception that get the response.body , status code, status text
    }


    return responseBody ;
}
//
// export async function update(app) {
//     const result = await axios.put(app.url, app);
//     return result.data;
// }
//
// export async function remove(app) {
//     return await axios.delete(app.url);
// }