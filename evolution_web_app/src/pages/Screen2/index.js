import UserList from "./UserList";
import {useEffect, useState} from "react";
import * as Screen2Services from "../../services/Screen2Services";

const Index = () => {
    const [users, setUsers] = useState([]);

    //TODO need to get all the information all the time
    //we'll later add a dependency to the useEffect that will change every x seconds and that will create a pull

    useEffect(() => {
        const fetchAllData = async () => {
            // calling all API calls in parallel, and waiting until they ALL finish before setting
            try {
                const [userList] = await Promise.all([
                    Screen2Services.getAll(),
                ]);
                setUsers(userList);
            } catch (e) {
                // setAlertText('Failed initializing app, please reload page');
            } finally {
                // setIsFetching(false);
            }
        };

        fetchAllData();
    }, []);

    return (
        <UserList/>
    )
}

export default Index;