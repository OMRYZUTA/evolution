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
import OtherSolutions from "./OtherSolutions";
import Typography from "@mui/material/Typography";

const fakeAlgoConfig = {
    timetableID: 0, //notice which timetable
    stride: "10",
    endPredicates: {numOfGenerations: "700", fitnessScore: "97.1", time: "4"},
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
        spacing: 2,
        justifyContent: "space-between",
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
    const [isRunning, setIsRunning] = useState(false);// flag use to diable and enable button
    const [timetable, setTimetable] = useState();
    const [otherSolutions, setOtherSolutions] = useState([]);
    const [progress, setProgress] = useState();
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
    const [open, setOpen] = React.useState(false);

    useEffect(() => {
        // calling all API calls in parallel, and waiting until they ALL finish before setting
        const fetchAllData = async () => {
            try {
                const [timetableResult, algoConfigResult, otherSolutionsResult, progressResult] = await Promise.all([
                    Screen3Services.getTimetableDetails(currentTimetableID),
                    Screen3Services.getAlgoConfig(currentTimetableID),
                    Screen3Services.getOtherSolutionsInfo(currentTimetableID),
                    Screen3Services.getProgress(currentTimetableID),
                ]);

                if (timetableResult.data) {
                    console.log("timetableResult result (screen 3 index)")
                    console.log(timetableResult.data);
                    setTimetable(timetableResult.data);
                } else {
                    // setAlertText('Failed initializing app, please reload page');
                    console.log("timetableResult.data is null");
                    console.log(timetableResult.error);
                }

                if (algoConfigResult.data) {
                    console.log("algoConfig result (screen 3 index)")
                    console.log(algoConfigResult.data);
                    setAlgorithmConfiguration(algoConfigResult.data);
                } else {
                    // setAlertText('Failed initializing app, please reload page');
                    console.log("algoConfigResult.data is null");
                    console.log(algoConfigResult.error);
                }

                if (otherSolutionsResult.data) {
                    console.log("otherSolutionsResult (screen 3 index)")
                    console.log(otherSolutionsResult.data);
                    setOtherSolutions(otherSolutionsResult.data);
                } else {
                    // setAlertText('Failed initializing app, please reload page');
                    console.log("otherSolutionsResult.data is null");
                    console.log(otherSolutionsResult.error);
                }

                if (progressResult.data) {
                    console.log("progressResult (screen 3 index)")
                    console.log(progressResult.data);
                    setProgress(progressResult.data);
                } else {
                    // setAlertText('Failed initializing app, please reload page');
                    console.log("progressResult.data is null");
                    console.log(progressResult.error);
                }

            } catch (e) {
                console.log(e);
                // setAlertText('Failed initializing app, please reload page');
            }
        };

        const interval = setInterval(() => {
            fetchAllData();
        }, 5000) //will run every 5 seconds

        return () => clearInterval(interval); // in order to clear the interval when the component unmounts.
    }, []);

    const handleStart = useCallback(async () => {
        setIsRunning(true);
        try {
            await Utils.fetchWrapper(
                'POST',
                `/server_Web_exploded/api/actions?action=start`,
                algorithmConfiguration);
        } catch (e) {
            // TODO handle exception add alert
            console.log(e);
        }

    }, [algorithmConfiguration]);

    const handlePause = useCallback(async () => {
        setIsRunning(false);
        try {
            // await Utils.fetchWrapper(
            //     'POST',
            //     `/server_Web_exploded/api/actions?action=pause`,
            //     currentTimetableID)

            await Utils.fetchWrapper(
                'POST',
                `/server_Web_exploded/api/actions?action=pause`,
                algorithmConfiguration)
        } catch (e) {
            // TODO handle exception add alert
            console.log(e);
        }
    }, [algorithmConfiguration]);

    const handleStop = useCallback(async () => {
        setIsRunning(false);
        try {
            await Utils.fetchWrapper(
                'POST',
                `/server_Web_exploded/api/actions?action=stop`,
                algorithmConfiguration)
        } catch (e) {
            // TODO handle exception add alert
            console.log(e);
        }
    }, [algorithmConfiguration]);

    const handleResume = useCallback(async () => {
        setIsRunning(true);
        try {
            await Utils.fetchWrapper(
                'POST',
                `/server_Web_exploded/api/actions?action=resume`,
                algorithmConfiguration)
        } catch (e) {
            // TODO handle exception add alert
            console.log(e);
        }
    }, [algorithmConfiguration]);

    const renderProgress = () => {
        if (progress) {
            return (
                <Grid container className={classes.settings}>
                    <Typography sx={{fontSize: 14}} color="text.secondary" gutterBottom>
                        {progress.generationNum}
                    </Typography>
                    <Typography sx={{fontSize: 14}} color="text.secondary" gutterBottom>
                        {progress.bestScoreInGeneration}
                    </Typography>
                </Grid>
            );
        }
    }

    const routeChange = () => {
        history.push(SCREEN2URL);
    }

    // const handleClickOpen = () => {
    //     console.log(" in handleClickOpen");
    //     setOpen(true);
    // };

    // const handleClose = (value) => {
    //     setOpen(false);
    //     // setSelectedValue(value);
    // };

    const renderButtonGroup = () => {
        return (
            <ButtonGroup
                aria-label="outlined primary button group">
                <Button id="start" onClick={handleStart} disabled={isRunning}>
                    start
                </Button>
                <Button id="pause" disabled={!isRunning} onClick={handlePause}>
                    pause
                </Button>
                <Button id="resume" disabled={isRunning} onClick={handleResume}>
                    resume
                </Button>
                <Button id="stop" disabled={!isRunning} onClick={handleStop}>
                    stop
                </Button>
                <Button id="bestSolution">
                    Best Solution
                </Button>
                {/*<SolutionDialog*/}
                {/*    // selectedValue={selectedValue}*/}
                {/*    open={open}*/}
                {/*    onClose={handleClose}*/}
                {/*/>*/}
                <Button id="back to screen 2" onClick={routeChange}>
                    Back to screen 2
                </Button>
            </ButtonGroup>
        );
    }

    return (
        <Grid>
            <Container maxWidth="xl">
                <Navbar user={currentUser}/>
                <Grid container direction={"row"} spacing={2}>

                    <Grid item xs={12} md={6}>
                        <Grid container direction={"column"} className={classes.tempGrid}>
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
                                <Paper>
                                    <Grid container direction={"column"} className={classes.root}>
                                        <Typography> progress </Typography>
                                        {renderProgress()}
                                    </Grid>
                                </Paper>
                            </Grid>

                            <Grid item>
                                <OtherSolutions otherSolutionsList={otherSolutions}/>
                            </Grid>

                        </Grid>
                    </Grid>
                </Grid>
            </Container>
        </Grid>
    );
}

export default Screen3;