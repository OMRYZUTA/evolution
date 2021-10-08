import {fetchWrapper} from "./Utils";

export const getTimetableDetails = async (timetableID) => {
    const url = `/server_Web_exploded/api/timetable/details?timetableID=${timetableID}`;
    const method = 'GET';
    return await fetchWrapper(method, url);
}

export const getAlgoConfig = async (timetableID) => {
    const url = `/server_Web_exploded/api/algoconfig?timetableID=${timetableID}`;
    const method = 'GET';
    return await fetchWrapper(method, url);
}

export const getOtherSolutionsInfo = async (timetableID) => {
    const url = `/server_Web_exploded/api/othersolutionsinfo?timetableID=${timetableID}`;
    const method = 'GET';
    return await fetchWrapper(method, url);
}

export const getProgress = async (timetableID) => {
    const url = `/server_Web_exploded/api/actions?timetableID=${timetableID}`;
    const method = 'GET';
    return await fetchWrapper(method, url);
}

export const getBestSolution = async (timetableID) => {
    const url = `/server_Web_exploded/api/bestsolution?timetableID=${timetableID}`;
    const method = 'GET';
    return await fetchWrapper(method, url);
}

export const postAction = async (action, algorithmConfiguration) => {
    const url = `/server_Web_exploded/api/actions?action=${action}`;
    const method = 'POST';
    const {data, error} = await fetchWrapper(method, url, algorithmConfiguration);
    if (error) {
        throw new Error(error);
    }

    return data;
}
