import * as Utils from "./Utils";

export const getOtherUsersInfoSolving = async (timetableID) => {
    const url = `/server_Web_exploded/api/otherusersinfo?timetableID=${timetableID}`;
    const method = 'GET';
    const result = await Utils.fetchWrapper(method, url);
    return result;
}

export const getTimetableDetails = async (timetableID) => {
    const url = `/server_Web_exploded/api/timetable/details?timetableID=${timetableID}`;
    const method = 'GET';
    const result = await Utils.fetchWrapper(method, url);
    return result;
}

export const getAlgoConfig = async (timetableID) => {
    const url = `/server_Web_exploded/api/algoconfig?timetableID=${timetableID}`;
    const method = 'GET';
    const result = await Utils.fetchWrapper(method, url);
    return result;
}
