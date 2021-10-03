import Button from '@mui/material/Button';
import {ButtonGroup, Container, Grid} from "@mui/material";
import InfoTabs from "./InfoTabs";
import {makeStyles} from "@mui/styles";
import React, {useCallback, useContext, useEffect, useState} from 'react';
import Navbar from "../../components/Navbar";
import Paper from "@mui/material/Paper";
import {TimetableContext} from "../../components/TimetableContext";
import {UserContext} from "../../components/UserContext"
import * as Utils from "../../services/Utils";
import {useHistory} from "react-router-dom";
import * as Screen3Services from "../../services/Screen3Services";

const fakeAlgoConfig = {
    timetableID: 0,
    stride: "10",
    endPredicates: {numOfGenerations: "120", fitnessScore: "97.1", time: "2"},
    engineSettings: {
        populationSize: "60",
        selection: {name: "RouletteWheel", elitism: "5"},
        crossover: {name: "DaytimeOriented", "cuttingPoints": "5"},
        mutations: [
            {name: "Flipping", probability: "0.2", maxTuples: "4", component: "H"}],
    }
}

const SCREEN2URL = "/server_Web_exploded/screen2";

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
    const {currentTimetableID} = useContext(TimetableContext);
    const [timetable, setTimetable] = useState();
    const [] = useState();
    const classes = useStyles();
    const history = useHistory();
    const emptyAlgoConfig = {
        timetableID: currentTimetableID,
        stride: undefined,
        endPredicates: {numOfGenerations: undefined, fitnessScore: undefined, time: undefined},
        engineSettings: {
            populationSize: undefined,
            selection: {name: "RouletteWheel", elitism: undefined},
            crossover: {name: "DaytimeOriented", "cuttingPoints": undefined},
            mutations: [],
        }
    }
    const [algorithmConfiguration, setAlgorithmConfiguration] = useState(emptyAlgoConfig);
    // const actions = ["start ", "pause ", "resume ", "stop "]

    useEffect(() => {
        // calling all API calls in parallel, and waiting until they ALL finish before setting
        const fetchAllData = async () => {
            try {
                const [timetableResult, algoConfigResult, otherUsersInfoResult] = await Promise.all([
                    Screen3Services.getTimetableDetails(currentTimetableID),
                    Screen3Services.getAlgoConfig(currentTimetableID),
                    null,//TODO restore later
                    // Screen3Services.getOtherUsersInfoSolving(currentTimetableID),
                ]);

                if (timetableResult.data) {
                    console.log(timetableResult.data);
                    setTimetable(timetableResult.data);
                } else {
                    // setAlertText('Failed initializing app, please reload page');
                    console.log(timetableResult.error);
                }

                if (algoConfigResult.data) {
                    console.log("algoConfig result (screen 3 index)")
                    console.log(algoConfigResult.data);
                    setAlgorithmConfiguration(algoConfigResult.data);
                } else {
                    // setAlertText('Failed initializing app, please reload page');
                    console.log(algoConfigResult.error);
                }

                // if (otherUsersInfoResult.data) {
                //     console.log(otherUsersInfoResult.data);
                //     setOtherUsersInfo(otherUsersInfoResult.data);
                // } else {
                //     // setAlertText('Failed initializing app, please reload page');
                //     // console.log(otherUsersInfoResult.error);
                // }
            } catch (e) {
                console.log(e);
                // setAlertText('Failed initializing app, please reload page');
            }
        };

        const interval = setInterval(() => {
            fetchAllData();
        }, 10000) //will run every 10 seconds

        return () => clearInterval(interval); // in order to clear the interval when the component unmounts.

    }, []);

    const handleStart = useCallback(async () => {
        console.log({algorithmConfiguration});
        let result;
        try {
            result = await Utils.fetchWrapper(
                'POST',
                `/server_Web_exploded/api/actions?action=start`,
                algorithmConfiguration)
            console.log({result});
        } catch (e) {
            console.log(e);
        }
        return result;
    }, [algorithmConfiguration]);

    const routeChange = () => {
        history.push(SCREEN2URL);
    }

    const renderButtonGroup = () => {
        return (<ButtonGroup
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
            <Button id="back to screen 2" onClick={routeChange}>
                Back to screen 2
            </Button>
        </ButtonGroup>);
    }

    return (
        <Grid>
            <Container maxWidth="xl">
                <Navbar user={currentUser}/>

                <Grid container direction={"row"} spacing={2}>

                    <Grid item xs={12} md={6}>
                        <Grid container direction={"column"} className={classes.tempGrid}>
                            {/*const InfoTabs = ({stats, algorithmConfiguration, handleAlgorithmConfigChange})*/}
                            <InfoTabs algorithmConfiguration={algorithmConfiguration}
                                      handleAlgorithmConfigSave={setAlgorithmConfiguration}
                                      timetable={timetable}/>
                        </Grid>
                    </Grid>

                    <Grid item xs={12} md={5}>
                        <Grid container direction={"column"} className={classes.tempGrid}>
                            <Grid item>
                                {renderButtonGroup()}
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
    )
}

export default Screen3;