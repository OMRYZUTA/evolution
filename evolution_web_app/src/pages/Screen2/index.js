import Alert from "@mui/material/Alert";
import AlertTitle from "@mui/material/AlertTitle";
import Box from "@mui/material/Box";
import Button from '@mui/material/Button';
import CloseIcon from '@mui/icons-material/Close';
import Grid from '@mui/material/Grid';
import {IconButton} from "@mui/material";
import {makeStyles} from '@mui/styles';
import Navbar from "../../components/Navbar";
import React, {useContext, useEffect, useState} from "react";
import Summary from "./Summary";
import {UserContext} from "../../components/UserContext";
import UserList from "./UserList";
import * as FileServices from "../../services/FileServices";
import * as Screen2Services from "../../services/Screen2Services";
import Typography from "@mui/material/Typography";

const useStyles = makeStyles((theme) => ({
    root: {
        padding: '10px 50px',
        minHeight: '50vh',
        backgroundColor: "pink",
    },
    aroundButton: {
        padding: '10px 50px',
        backgroundColor: "pink",
    },
    summaries: {
        padding: '5px 5px',
        alignItems: 'top-center',
        backgroundColor: 'lightyellow',
    },
    button: {
        backgroundColor: 'pink',
        margin: '5px',
    },
}))

const Index = () => {
    const {currentUser} = useContext(UserContext);
    const classes = useStyles();
    const [users, setUsers] = useState([]);
    const [summaries, setSummaries] = useState([]);
    const [alertText, setAlertText] = React.useState('');
    const [alertType,setAlertType] = useState("error");
    const [alertHeader,setAlertHeader] = useState("Error")

    useEffect(() => {
        const fetchAllData = async () => {
            // calling all API calls in parallel, and waiting until they ALL finish before setting
            try {
                const dashboardPayload = await Screen2Services.getAll();
                setUsers(dashboardPayload.users);
                setSummaries([...dashboardPayload.timetables]);
            } catch (e) {
                console.error("failed fetching all data", e);
                setAlertText('oops something went wrong, please reload page');
            }
        };

        const interval = setInterval(() => {
            fetchAllData();
        }, 5000) //todo return to 1 sec
        fetchAllData()//and initially
        return () => clearInterval(interval); // in order to clear the interval when the component unmounts.
    }, []);

    const renderAlert = () => {
        return (
            <Box sx={{width: '100%'}}>
                <Alert severity={alertType}
                       action={
                           <IconButton
                               aria-label="close"
                               color="inherit"
                               size="small"
                               onClick={() => {
                                   setAlertText('');
                               }}>
                               <CloseIcon fontSize="inherit"/>
                           </IconButton>
                       }
                       sx={{mb: 2}}>
                    <AlertTitle>{alertHeader}</AlertTitle>
                    {alertText}
                </Alert>
            </Box>
        );
    }

    const handleFileUpload = async (event) => {
        const file = event.target.files[0];
        event.target.value = '';
        const result = await FileServices.uploadFile(file);
        if (result !== "OK") {
            setAlertText(result);
            setAlertType("error");
            setAlertHeader("Error");
        }
        else{
            setAlertText("File was uploaded successfully");
            setAlertType("success");
            setAlertHeader("Success");
            setTimeout(()=>{
                setAlertText("");
            },2000)
        }
    };

    return (
        <Grid container direction={"column"}>
            <Navbar user={currentUser}/>
            <Grid item
                  className={classes.aroundButton}
                  alignItems="top-center"
                  justifyContent="flex-start"
                  spacing={2}>
                <label htmlFor="contained-button-file">
                    <input hidden accept=".xml" id="contained-button-file" type="file"
                           onChange={handleFileUpload}/>
                    <Button className={classes.button} variant="contained" component="span">
                        Upload File
                    </Button>
                </label>
                {alertText && renderAlert(alertText)}
            </Grid>

            <Grid container
                  className={classes.root}
                  direction="row"
                  justifyContent="space-between"
                  spacing={2}>
                <Grid item xs={12} md={3}>
                    <Typography>Users</Typography>
                    <UserList users={users}/>
                </Grid>

                <Grid item xs={12} md={9}>
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
