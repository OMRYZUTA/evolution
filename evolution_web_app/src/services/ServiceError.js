export default class ServiceError extends Error {
    #method
    #url
    #statusCode
    #statusText
    #body

    constructor(method, url, statusCode, statusText, body, ...params) {
        // Pass remaining arguments (including vendor specific ones) to parent constructor
        super(...params);

        // Maintains proper stack trace for where our error was thrown (only available on V8)
        if (Error.captureStackTrace) {
            Error.captureStackTrace(this, ServiceError);
        }

        this.#method = method;
        this.#url = url;
        this.#statusCode = statusCode;
        this.#statusText = statusText;
        this.#body = body
    }

    get name() {
        return "ServiceError";
    }

    get method() {
        return this.#method;
    }

    get url() {
        return this.#url;
    }

    get statusCode() {
        return this.#statusCode;
    }

    get statusText() {
        return this.#statusText;
    }

    get body() {
        return this.#body;
    }
}
