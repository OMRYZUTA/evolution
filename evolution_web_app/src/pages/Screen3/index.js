import {Container, Grid,} from '@mui/material';
import Navbar from "../../components/Navbar";
import {UserContext} from "../../components/UserContext"
import {useContext, useEffect, useState} from "react";
import {makeStyles} from "@material-ui/core/styles";
import Button from '@material-ui/core/Button';
import Paper from "@mui/material/Paper";
import {TimetableContext} from "../../components/TimetableContext";
import {ButtonGroup} from "@material-ui/core";
import * as Screen2Services from "../../services/Screen2Services";
import * as TimetableServices from "../../services/TimetableServices";

const useStyles = makeStyles((theme) => ({
    root: {
        padding: '50px 70px',
        spacing: 2,
        justifyContent: 'flex-start',
        alignItems: 'top-center',
        minHeight: "100vh",
    },
    settings: {
        width: '100%',
        height: 400,
        maxWidth: 300,
        backgroundColor: "#D3D3D3", //light gray
    },
    actions: {
        padding: 20,
        margin: 20,
        justifyContent: 'space-evenly',
    },
    tempGrid: {
        padding: 20,
        margin: 20,
        backgroundColor: "pink",
    },
}));

const Screen3 = () => {
    const {currentUser} = useContext(UserContext);
    const {currentTimetable} = useContext(TimetableContext);//todo change to id
    const [timetable,setTimetable] = useState();
    const classes = useStyles();
    const actions = ["start ", "pause ", "resume ", "stop "]
    useEffect(() => {

        const fetchAllData = async () => {
            // calling all API calls in parallel, and waiting until they ALL finish before setting
            try {
                const result = await TimetableServices.getDetails(currentTimetable);
                if(result.data){
                    setTimetable(result.data);
                }
                else{
                    console.log(result.error);
                }

            } catch (e) {
                console.log(e);
                // setAlertText('Failed initializing app, please reload page');
            }
        };
        fetchAllData();
    }, []);

    return (
        <Grid>
            <Container maxWidth="xl">
                <Navbar user={currentUser}/>
                <Grid container direction={"row"} spacing={2}>
                    <Grid item xs={12} md={5}>
                        <Grid container direction={"column"} className={classes.tempGrid}>
                            <Grid item>
                                <Paper>timetable details</Paper>
                            </Grid>
                            <Grid item>
                                <Paper>configuration</Paper>
                            </Grid>
                        </Grid>
                    </Grid>

                    <Grid item xs={12} md={5}>
                        <Grid container direction={"column"} className={classes.tempGrid}>
                            <Grid item>
                                <ButtonGroup
                                    aria-label="outlined primary button group">
                                    <Button
                                        id="start">
                                        start
                                    </Button>
                                    <Button id="pause">
                                        pause
                                    </Button>
                                    <Button id="resume">
                                        resume
                                    </Button>
                                    <Button id="stop">
                                        stop
                                    </Button>
                                    <Button id="bestSolution">
                                        Best Solution
                                    </Button>
                                    <Button id="back to screen 2">
                                        Back to screen 2
                                    </Button>
                                </ButtonGroup>
                            </Grid>
                            <Grid item>
                                <Paper>stride details</Paper>
                            </Grid>
                            <Grid item>
                                <Paper>other users solving</Paper>
                            </Grid>
                        </Grid>
                    </Grid>
                </Grid>
            </Container>
        </Grid>
    );
}

export default Screen3;