import UserList from "./UserList";
import {useEffect, useState} from "react";
import { makeStyles } from '@material-ui/core/styles';
import Grid from '@material-ui/core/Grid';
import * as Screen2Services from "../../services/Screen2Services";
import Summary from "./Summary";

const useStyles = makeStyles((theme) => ({
    root: {
        padding: '50px 70px',
        spacing: 2,
        justifyContent: 'flex-start',
        alignItems: 'top-center',
        minHeight: "100vh",
    },

}))
const fakeData = [{
    username:"Hagit",
    days:4,
    hours:5,
    schoolclasses:3,
    teachers:6,
    hardRules:3,
    softRules:7,
    solvingUsers:2,
    maxFitnessSoFar:88.2,
},
    {
        username:"Bar",
        days:6,
        hours:5,
        schoolclasses:6,
        teachers:12,
        hardRules:2,
        softRules:9,
        solvingUsers:3,
        maxFitnessSoFar:89.4,
    }]

const Index = () => {
    const classes = useStyles();
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
                console.log({userList});
                setUsers(userList.users);
            } catch (e) {
                console.log(e);
                // setAlertText('Failed initializing app, please reload page');
            } finally {
                // setIsFetching(false);
            }
        };

        fetchAllData();

    }, []);

    return (
        <Grid container className ={classes.root} direction={"row"} spacing={2}>
            <Grid item>
        <UserList users ={users}/>
            </Grid>
            <Grid item>
                <Grid container direction={"row"} >
                    {fakeData.map(summary => {
                        return (
                            <Summary data={summary}/>
                        )
                    })}
                </Grid>
            </Grid>
        </Grid>
    )
}
export default Index;
