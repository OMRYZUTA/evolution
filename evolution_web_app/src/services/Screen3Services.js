import {fetchWrapper} from "./Utils";


export const getTimetableDetails = async (timetableID) => {
    const url = `/evolution/api/timetable/details?timetableID=${timetableID}`;
    const method = 'GET';
    return await fetchWrapper(method, url);
}

export const getAlgoConfig = async (timetableID) => {
    const url = `/evolution/api/algoconfig?timetableID=${timetableID}`;
    const method = 'GET';
    return await fetchWrapper(method, url);
}

export const getOtherSolutionsInfo = async (timetableID) => {
    const url = `/evolution/api/othersolutionsinfo?timetableID=${timetableID}`;
    const method = 'GET';
    return await fetchWrapper(method, url);
}

export const getProgress = async (timetableID) => {
    const url = `/evolution/api/actions?timetableID=${timetableID}`;
    const method = 'GET';
    return await fetchWrapper(method, url);
}

//best solution from all users solving problem
export const getBestSolution = async (timetableID) => {
    const url = `/evolution/api/bestsolution?timetableID=${timetableID}`;
    const method = 'GET';
    return await fetchWrapper(method, url);
}

//best solution from user
export const getBestUserSolution = async (timetableID) => {
    const url = `/evolution/api/bestusersolution?timetableID=${timetableID}`;
    const method = 'GET';
    return await fetchWrapper(method, url);
}

export const getStrideData = async (timetableID) => {
    const url = `/evolution/api/stridedata?timetableID=${timetableID}`;
    const method = 'GET';
    return await fetchWrapper(method, url);
}

export const postAction = async (action, algorithmConfiguration) => {
    const url = `/evolution/api/actions?action=${action}`;
    const method = 'POST';
    const {data, error} = await fetchWrapper(method, url, algorithmConfiguration);
    if (error) {
        throw new Error(error);
    }

    return data;
}
