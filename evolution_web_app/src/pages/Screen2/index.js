import UserList from "./UserList";
import React, {useContext, useEffect, useState} from "react";
import {makeStyles} from '@mui/styles';
import Grid from '@mui/material/Grid';
import * as Screen2Services from "../../services/Screen2Services";
import Summary from "./Summary";
import Navbar from "../../components/Navbar";
import Button from '@mui/material/Button';
import * as FileServices from "../../services/FileServices";
import {UserContext} from "../../components/UserContext";
import Alert from "@mui/material/Alert";
import AlertTitle from "@mui/material/AlertTitle";

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
const renderAlert = (alertText) => {
    return (
        <Alert severity="error">
            <AlertTitle>Error</AlertTitle>
            {alertText}
        </Alert>
    );
}

const Index = () => {
    const {currentUser} = useContext(UserContext);
    const classes = useStyles();
    const [users, setUsers] = useState([]);
    const [summaries, setSummaries] = useState([]);
    const [selectedFile, setSelectedFile] = useState();
    const [alertText, setAlertText] = React.useState('');
    //TODO need to get all the information all the time
    //we'll later add a dependency to the useEffect that will change every x seconds and that will create a pull

    useEffect(() => {
        const fetchAllData = async () => {
            // calling all API calls in parallel, and waiting until they ALL finish before setting
            try {
                const dashboardPayload = await Screen2Services.getAll();
                setUsers(dashboardPayload.users);
                setSummaries([ ...dashboardPayload.timetables]);
            } catch (e) {
                console.log(e);
                // setAlertText('Failed initializing app, please reload page');
            } finally {
                // setIsFetching(false);
            }
        };
        const interval = setInterval(() => {
            fetchAllData();
        }, 10000) //todo return to 1 sec
        fetchAllData()//and initially
        return () => clearInterval(interval); // in order to clear the interval when the component unmounts.
    }, []);

    const handleFileUpload = async (event) => {
        setSelectedFile(event.target.files[0]);
        const result = await FileServices.uploadFile(event.target.files[0]);
        if(result !=="OK"){
            setAlertText(result);
        }
        console.log(result);//todo handle bad xml
    };

    return (
        <Grid container direction={"column"}>
            <Navbar user={currentUser}/>
            <Grid item>
                <Button
                    variant="contained"
                    component="label"
                >
                    Upload File
                    <input
                        type="file"
                        name={"file"}
                        onChange={handleFileUpload}
                        hidden
                        accept={".xml"}
                    />
                </Button>
                {alertText && renderAlert(alertText)}
            </Grid>
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
