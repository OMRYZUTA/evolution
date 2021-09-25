import * as Utils from "./Utils";

export async function uploadFile(file) {
    const url ="/server_Web_exploded/api/dashboard";
    const method = 'POST';
    const result = await fetchXmlWrapper(method, url, file);
    return result ;
}
const fetchXmlWrapper =async  (method, url, file)=> {
    const options = {
        method,
        headers: {
            'Content-Type': 'application/xml',
        },
        body:file,
        redirect: 'follow',
        referrerPolicy: 'no-referrer',
    }


    const result = await fetch(url, options);
    let responseBody;
    if (result.headers.get("content-type").includes("application/json")) {
        responseBody = await result.json();
    } else {
        responseBody = await result.text();
        console.log(responseBody);
    }
    if (!result.ok) {
        console.log("error");
        throw  new Error("error") //todo create an exception that get the response.body , status code, status text
    }

    return responseBody;
}