export async function fetchWrapper(method, url, object) {
    const options = {
        method,
        headers: {'Content-Type': 'application/json',},
        redirect: 'follow',
        referrerPolicy: 'no-referrer',
    }

    if (object) {
        options.body = JSON.stringify(object);
    }

    const result = await fetch(url, options);
    let responseBody;

    if (result.headers.get("content-type").includes("application/json")) {
        responseBody = await result.json();
    } else {
        responseBody = await result.text();
    }
    if (!result.ok) {
        throw new Error("error") //todo create an exception that get the response.body , status code, status text
    }

    return responseBody;
}