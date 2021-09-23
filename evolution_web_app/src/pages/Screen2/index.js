import UserList from "./UserList";
import {useContext, useEffect, useState} from "react";
import {useLocation} from 'react-router-dom';
import {makeStyles} from '@material-ui/core/styles';
import Grid from '@material-ui/core/Grid';
import * as Screen2Services from "../../services/Screen2Services";
import Summary from "./Summary";
import Navbar from "../../components/Navbar"
import {UserContext} from "../../components/UserContext";
const useStyles = makeStyles((theme) => ({
    root: {
        padding: '50px 70px',
        spacing: 2,
        justifyContent: 'flex-start',
        alignItems: 'top-center',
        minHeight: "100vh",
    },
    summaries: {
        alignItems: 'top-center',
    },

}))
const fakeData = [{ //todo delete later
    username: "Hagit",
    days: 4,
    hours: 5,
    schoolclasses: 3,
    teachers: 6,
    hardRules: 3,
    softRules: 7,
    solvingUsers: 2,
    maxFitnessSoFar: 88.2,
},
    {
        username: "Bar",
        days: 6,
        hours: 5,
        schoolclasses: 6,
        teachers: 12,
        hardRules: 2,
        softRules: 9,
        solvingUsers: 3,
        maxFitnessSoFar: 89.4,
    },
    {
        username: "Yafa",
        days: 6,
        hours: 5,
        schoolclasses: 6,
        teachers: 12,
        hardRules: 2,
        softRules: 9,
        solvingUsers: 3,
        maxFitnessSoFar: 89.4,
    },
    {
        username: "Gad",
        days: 6,
        hours: 5,
        schoolclasses: 6,
        teachers: 12,
        hardRules: 2,
        softRules: 9,
        solvingUsers: 3,
        maxFitnessSoFar: 89.4,
    },
    {
        username: "Joseph",
        days: 6,
        hours: 5,
        schoolclasses: 6,
        teachers: 12,
        hardRules: 2,
        softRules: 9,
        solvingUsers: 3,
        maxFitnessSoFar: 89.4,
    }]

const Index = () => {
    const user = useContext(UserContext);
    const classes = useStyles();
    const [users, setUsers] = useState([]);
    const [summaries, setSummaries] = useState(fakeData);

    //TODO need to get all the information all the time
    //we'll later add a dependency to the useEffect that will change every x seconds and that will create a pull

    useEffect(() => {
        const fetchAllData = async () => {
            // calling all API calls in parallel, and waiting until they ALL finish before setting
            try {
                const [userList] = await Promise.all([
                    Screen2Services.getAll(),
                ]);
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
        <Grid container direction={"column"}>
            <Navbar user={user}/>
            <Grid container className={classes.root} direction={"row"}>
                <Grid item xs={12} md={4}>
                    <UserList users={users}/>
                </Grid>
                <Grid item xs={12} md={8}>
                    <Grid container direction={"row"} className={classes.summaries}>
                        {summaries.map(summary => {
                            return (
                                <Summary data={summary}/>
                            )
                        })}
                    </Grid>
                </Grid>
            </Grid>
        </Grid>
    )
}
export default Index;
