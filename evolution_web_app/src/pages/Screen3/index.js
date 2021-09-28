import React, {useCallback, useContext, useEffect, useState} from 'react';
import Button from '@material-ui/core/Button';
import {ButtonGroup} from "@material-ui/core";
import {Container, Grid,} from '@mui/material';
import InfoTabs from "./InfoTabs";
import {makeStyles} from "@material-ui/core/styles";
import Navbar from "../../components/Navbar";
import Paper from "@mui/material/Paper";
import {TimetableContext} from "../../components/TimetableContext";
import {UserContext} from "../../components/UserContext"
import * as TimetableServices from "../../services/TimetableServices";
import * as Utils from "../../services/Utils";

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

const fakeAlgoConfig = {
    timetableID: 0,
    populationSize: 32,
    stride: 2,
    endPredicates: [{"name": "numOfGenerations", value: 300},{"name": "numOfGenerations", value: 300},{"name": "numOfGenerations", value: 300}],
    engineSettings: {
        selection: {
            name: "rouletteWheel", elitism: 0
        },
        crossover: {name: "daytimeOriented", "cuttingPoints": 5},
        mutations: [{name: "flipping", probability: 0.2, maxTuples: 4, component: "H"}],
    }
}

const Screen3 = () => {
    const {currentUser} = useContext(UserContext);
    const {currentTimetable} = useContext(TimetableContext);//todo change to id
    const [timetable, setTimetable] = useState();
    const [algorithmConfiguration, setAlgorithmConfiguration] = useState(fakeAlgoConfig);
    const classes = useStyles();
    // const actions = ["start ", "pause ", "resume ", "stop "]

    useEffect(() => {
        const fetchAllData = async () => {
            try {
                const result = await TimetableServices.getDetails(currentTimetable);
                if (result.data) {
                    setTimetable(result.data);
                } else {
                    console.log(result.error);
                }
            } catch (e) {
                console.log(e);
                // setAlertText('Failed initializing app, please reload page');
            }
        };

        fetchAllData();
    }, []);

    const handleStart = useCallback(async () => {
        const url = `/server_Web_exploded/api/actions?action=start`;
        const bodyObject = {};
        const result = await Utils.fetchWrapper('POST', url, bodyObject)
    }, []);

    return (
        <Grid>
            <Container maxWidth="xl">
                <Navbar user={currentUser}/>
                <Grid container direction={"row"} spacing={2}>
                    <Grid item xs={12} md={6}>
                        <Grid container direction={"column"} className={classes.tempGrid}>
                            <InfoTabs engineSettings={algorithmConfiguration}
                                      handleEngineSettingsChanged={setAlgorithmConfiguration}/>
                        </Grid>
                    </Grid>

                    <Grid item xs={12} md={5}>
                        <Grid container direction={"column"} className={classes.tempGrid}>
                            <Grid item>
                                <ButtonGroup
                                    aria-label="outlined primary button group">
                                    <Button
                                        id="start" onClick={handleStart}>
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
                                    <Button id="back to screen 2" onClick={() => {
                                        console.log("onClick back to Screen2")
                                    }}>
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