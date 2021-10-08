import Button from '@mui/material/Button';
import {Alert, AlertTitle, ButtonGroup, Container, Grid, IconButton} from "@mui/material";
import InfoTabs from "./InfoTabs";
import {makeStyles} from "@mui/styles";
import React, {useCallback, useContext, useEffect, useState} from 'react';
import Navbar from "../../components/Navbar";
import {TimetableContext} from "../../components/TimetableContext";
import {UserContext} from "../../components/UserContext"
import {useHistory} from "react-router-dom";
import * as Screen3Services from "../../services/Screen3Services";
import OtherSolutions from "./OtherSolutions";
import Typography from "@mui/material/Typography";
import CircularIndeterminate from "../../components/CircularIndeterminate";
import SolutionDialog from "./SolutionDialog";
import Box from "@mui/material/Box";
import CloseIcon from "@mui/icons-material/Close";

const fakeAlgoConfig = {
    timetableID: 0, //important to notice which timetable we're dealing with
    stride: "10",
    endPredicates: {numOfGenerations: "700", fitnessScore: "97.1", time: "4"},
    engineSettings: {
        populationSize: "60",
        selection: {type: "RouletteWheel", elitism: "5"},
        crossover: {type: "DayTimeOriented", "cuttingPoints": "5"},
        mutations: [
            {type: "Flipping", probability: "0.2", maxTuples: "4", component: "H"}],
    }
}

const ERROR = Symbol.for('ERROR');
const RUNNING = Symbol.for('RUNNING');
const STOPPED = Symbol.for('STOPPED');
const PAUSED = Symbol.for('PAUSED');

const UNSAVED = Symbol.for('UNSAVED');
const SAVED = Symbol.for('SAVED');
const DIRTY = Symbol.for('DIRTY');

const SCREEN2URL = "/server_Web_exploded/screen2";

const useStyles = makeStyles((theme) => ({
    root: {
        padding: '50px 70px',
        spacing: 2,
        justifyContent: 'flex-start',
        alignItems: 'top-center',
    },
    settings: {
        spacing: 2,
        justifyContent: "space-between",
        width: '100%',
        height: 400,
        maxWidth: 300,
        backgroundColor: "#D3D3D3", //light gray
    },
    progressRow: {
        spacing: 2,
        justifyContent: "space-between",
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
    const [otherSolutions, setOtherSolutions] = useState([]);
    const [progress, setProgress] = useState();
    const [bestSolution, setBestSolution] = useState();
    const classes = useStyles();
    const history = useHistory();
    const emptyAlgoConfig = {
        timetableID: currentTimetableID,
        stride: undefined,
        endPredicates: {numOfGenerations: undefined, fitnessScore: undefined, time: undefined},
        engineSettings: {
            populationSize: undefined,
            selection: {type: "RouletteWheel", elitism: undefined},
            crossover: {type: "DayTimeOriented", "cuttingPoints": undefined},
            mutations: [],
        }
    }
    const [algorithmConfiguration, setAlgorithmConfiguration] = useState(emptyAlgoConfig);
    const [alertText, setAlertText] = React.useState('');
    const [isFetching, setIsFetching] = React.useState(true);
    const [runStatus, setRunStatus] = useState(STOPPED);
    const [saveStatus, setSaveStatus] = useState(UNSAVED);
    const [open, setOpen] = React.useState(false);

    useEffect(() => {
        // calling all API calls in parallel, and waiting until they ALL finish before setting
        const fetchStaticData = async () => {
            try {
                const [timetableResult, algoConfigResult] = await Promise.all([
                    Screen3Services.getTimetableDetails(currentTimetableID),
                    Screen3Services.getAlgoConfig(currentTimetableID),
                ]);

                if (timetableResult.data) {
                    setTimetable(timetableResult.data);
                } else {
                    // setAlertText('oops something went wrong, please reload page');
                    console.log(timetableResult.error);
                }

                if (algoConfigResult.data) {
                    // setAlertText('oops something went wrong, please reload page');
                    setAlgorithmConfiguration(algoConfigResult.data);
                } else {
                    console.log("algoConfigResult.data is null");
                    console.log(algoConfigResult.error);
                }
            } catch (e) {
                setAlertText('oops something went wrong, please reload page\n ' + e.message);
                console.log(e);
            } finally {
                setIsFetching(false);
            }
        }

        const fetchIntervalData = async () => {
            try {
                const [otherSolutionsResult, progressResult, bestSolutionResult] = await Promise.all([
                    Screen3Services.getOtherSolutionsInfo(currentTimetableID),
                    Screen3Services.getProgress(currentTimetableID),
                    Screen3Services.getBestSolution(currentTimetableID)
                ]);

                if (otherSolutionsResult.data) {
                    setOtherSolutions(otherSolutionsResult.data);
                } else {
                    console.log("otherSolutionsResult.data is null");
                    console.log(otherSolutionsResult.error);
                }

                if (progressResult.data) {
                    setProgress(progressResult.data);
                } else {
                    console.log("progressResult.data is null");
                    console.log(progressResult.error);
                }

                if (bestSolutionResult.data) {
                    setBestSolution(bestSolutionResult.data);
                } else {
                    console.log("bestSolutionResult.data is null");
                    console.log(bestSolutionResult.error);
                }

            } catch (e) {
                setAlertText('oops something went wrong, please reload page\n ' + e.message);
                console.log(e);
            }
        };

        fetchStaticData();
        fetchIntervalData();

        const interval = setInterval(() => {
            fetchIntervalData();
        }, 5000) //will run every 5 seconds

        //React performs the cleanup when the component unmounts.
        return () => clearInterval(interval); // in order to clear the interval when the component unmounts.
    }, [currentTimetableID]);

    const renderAlert = () => {
        return (
            <Box sx={{width: '100%'}}>
                <Alert severity="error"
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
                    <AlertTitle>Error</AlertTitle>
                    {alertText}
                </Alert>
            </Box>
        );
    };

    // useEffect(() => {
    //     setSaveStatus(!!algorithmConfiguration ? );
    // }, [algorithmConfiguration]);

    const handleStart = useCallback(async () => {
        try {
            await Screen3Services.postAction('start', algorithmConfiguration);
            setRunStatus(RUNNING);
        } catch (e) {
            setAlertText(e.message);
            console.log(e);
            setRunStatus(ERROR);
        }

    }, [algorithmConfiguration]);

    const handlePause = useCallback(async () => {
        try {
            await Screen3Services.postAction('pause', algorithmConfiguration);
            setRunStatus(PAUSED);
        } catch (e) {
            setAlertText(e.message);
            console.log(e);
            setRunStatus(ERROR);
        }
    }, [algorithmConfiguration]);

    const handleStop = useCallback(async () => {
        try {
            await Screen3Services.postAction('stop', algorithmConfiguration);
            setRunStatus(STOPPED);
        } catch (e) {
            setAlertText(e.message);
            console.log(e);
            setRunStatus(ERROR);
        }
    }, [algorithmConfiguration]);

    const handleResume = useCallback(async () => {
        try {
            await Screen3Services.postAction('resume', algorithmConfiguration);
            setRunStatus(RUNNING);
        } catch (e) {
            setAlertText(e.message);
            console.log(e);
            setRunStatus(ERROR);
        }
    }, [algorithmConfiguration]);

    const renderProgress = () => {
        return (
            <Grid container className={classes.progressRow}>
                {progress ?
                    [<Typography sx={{fontSize: 14}} color="text.secondary" gutterBottom>
                        {progress.generationNum}
                    </Typography>,
                        <Typography sx={{fontSize: 14}} color="text.secondary" gutterBottom>
                            {progress.bestScoreInGeneration}
                        </Typography>]
                    :
                    <Typography> Start running the algorithm to see some progress </Typography>}
            </Grid>
        );
    }

    const routeChange = () => {
        history.push(SCREEN2URL);
    }

    const handleSolutionClick = useCallback(() => {
        setOpen(true);
    }, [open])

    const handleClose = () => {
        setOpen(false);
        // setSelectedValue(value);
    };

    const startEnabled = true; //runStatus !== ERROR && runStatus === STOPPED;
    const pauseEnabled = true; //runStatus !== ERROR && runStatus === RUNNING;
    const resumeEnabled = true; //runStatus !== ERROR && runStatus === PAUSED;
    const stopEnabled = true; //runStatus !== ERROR && runStatus === RUNNING || runStatus === PAUSED;
    const renderButtonGroup = () => {
        return (
            <ButtonGroup
                aria-label="outlined primary button group">
                <Button id="start" disabled={!startEnabled} onClick={handleStart}>
                    start
                </Button>
                <Button id="pause" disabled={!pauseEnabled} onClick={handlePause}>
                    pause
                </Button>
                <Button id="resume" disabled={!resumeEnabled} onClick={handleResume}>
                    resume
                </Button>
                <Button id="stop" disabled={!stopEnabled} onClick={handleStop}>
                    stop
                </Button>
                <Button id="bestSolution" onClick={handleSolutionClick} disabled={!bestSolution}>
                    Best Solution
                </Button>
                {open && <SolutionDialog
                    handleClose={handleClose}
                    days={timetable.days}
                    hours={timetable.hours}
                    solution={bestSolution}
                    teachers={timetable.teachers}
                />}
                <Button id="back to screen 2" onClick={routeChange}>
                    Back to screen 2
                </Button>
            </ButtonGroup>
        );
    }

    return (
        <Grid>
            <Grid item>
                {isFetching && <CircularIndeterminate/>}
            </Grid>
            <Container maxWidth="xl">
                <Navbar user={currentUser}/>
                <Grid item
                      alignItems="top-center"
                      justifyContent="flex-start"
                      spacing={2}>
                    {alertText && renderAlert(alertText)}
                </Grid>
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
                                <Grid container direction={"column"}>
                                    <Grid item>
                                        <Typography> My Progress (best score in generation) </Typography>
                                        {renderProgress()}
                                    </Grid>
                                    <Grid item>
                                        <OtherSolutions otherSolutionsList={otherSolutions}/>
                                    </Grid>
                                </Grid>
                            </Grid>
                        </Grid>
                    </Grid>
                </Grid>
            </Container>
        </Grid>
    );
}

export default Screen3;