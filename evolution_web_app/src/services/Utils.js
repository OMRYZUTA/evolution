import ServiceError from './ServiceError'

export async function buildResponse(result, method, url) {
    let responseBody;

    const contentType = result.headers.get("content-type");
    if (contentType && contentType.includes("application/json")) {
        responseBody = await result.json();
    } else {
        responseBody = await result.text();
    }

    if (!result.ok) {
        throw new ServiceError(method, url, result.statusCode, result.statusText, responseBody)
    }

    return responseBody;
}

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
    return await buildResponse(result, method, url);
}