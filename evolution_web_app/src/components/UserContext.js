import {createContext} from "react";
export const UserContext = createContext(document.cookie||null);// if we have a cookie, that the userContext value