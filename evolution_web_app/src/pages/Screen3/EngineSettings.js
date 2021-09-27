import Paper from "@mui/material/Paper";
import {Checkbox, FormControlLabel, IconButton, TextField} from "@material-ui/core";
import Grid from "@material-ui/core/Grid";
import {makeStyles} from "@material-ui/core/styles";
import DropDown from "../../components/Dropdown";
import AddIcon from '@material-ui/icons/Add';
import * as React from 'react';

import Accordion from '@mui/material/Accordion';
import AccordionSummary from '@mui/material/AccordionSummary';
import AccordionDetails from '@mui/material/AccordionDetails';
import Typography from '@mui/material/Typography';
import ExpandMoreIcon from '@mui/icons-material/ExpandMore';
import {useState} from "react";


const useStyles = makeStyles((theme) => ({
    root: {
        alignItems: "start",
        justifyContent: "space-between",
        padding: 20,
        backgroundColor: "#D3D3D3", //light gray
    },
}));
const fakeEngineSettings = {
    timetableID: 0,
    populationSize: 32,
    stride: 2,
    selection: {
        name: "rouletteWheel",
        elitism: 0
    },
    crossover: {name: "daytimeOriented", "cuttingPoints": 5},
    mutations: [{name: "flipping", probability: 0.2}],
    endPredicates: [{"name": "numOfGeneration", value: 300}]
}
const selections = [{name: "Truncation", id: "truncation"}, {
    name: "Roulette Wheel",
    id: "rouletteWheel"
}, {name: "Tournament", id: "tournament"}];
const crossovers = [{name: "Daytime oriented", id: "daytimeOriented"}, {name: "Aspect Oriented", id: "aspectOriented"}];
const orientations = [{name: "Teacher", id: "teacher"}, {name: "Class", id: "class"}];
const mutations = [{name: "Flipping", id: "flipping"}, {name: "Sizer", id: "sizer"}];
const flippingComponent = [{name: "Hour", id: "H"}, {name: "Day", id: "D"}, {name: "Teacher", id: "T"}, {
    name: "Class",
    id: "C"
}, {name: "Subject", id: "S"},]

const EngineSettings = () => {
    const classes = useStyles();
    const [engineSettings, setEngineSettings] = useState(fakeEngineSettings);

    function renderSelectionExtraField() {
        if (engineSettings.selection.name === "tournament") {
            return (
                <TextField
                    required
                    id="outlined-required"
                    label="PTE"
                    defaultValue={engineSettings.selection.pte}
                />
            )
        } else {
            return (
                <TextField
                    required
                    id="outlined-required"
                    label="Top percent"
                    defaultValue={engineSettings.selection.topPercent}
                />
            )
        }
    }

    return (
        <Paper>
            <Accordion>
                <AccordionSummary
                    expandIcon={<ExpandMoreIcon/>}
                    aria-controls="panel1a-content"
                    id="panel1a-header"
                >
                    <Typography>General Details</Typography>

                </AccordionSummary>
                <AccordionDetails>
                    <Grid container className={classes.root}>
                        <TextField
                            required
                            id="outlined-required"
                            label="Population size"
                            defaultValue={engineSettings.populationSize}
                        />
                        <TextField
                            required
                            id="outlined-required"
                            label="stride"
                            defaultValue={engineSettings.stride}
                        />
                    </Grid>
                </AccordionDetails>
            </Accordion>
            <Accordion>
                <AccordionSummary
                    expandIcon={<ExpandMoreIcon/>}
                    aria-controls="panel2a-content"
                    id="panel2a-header"
                >
                    <Typography>Selection</Typography>
                </AccordionSummary>
                <AccordionDetails>
                    <Grid container className={classes.root}>
                        <DropDown
                            label={"Selection"}
                            options={selections}
                            currentValue={engineSettings.selection.name}
                            keyPropName="id"
                            namePropName="name"
                            onChange={() => console.log("selection changed")}
                        />
                        <TextField
                            required
                            id="outlined-required"
                            label="Elitism"
                            defaultValue={engineSettings.selection.elitism}
                        />
                        {engineSettings.selection.name === "rouletteWheel" || renderSelectionExtraField()}


                    </Grid>
                </AccordionDetails>
            </Accordion>
            <Accordion>
                <AccordionSummary
                    expandIcon={<ExpandMoreIcon/>}
                    aria-controls="panel2a-content"
                    id="panel2a-header"
                >
                    <Typography>Crossover</Typography>
                </AccordionSummary>
                <AccordionDetails>
                    <Grid container className={classes.root}>
                        <DropDown
                            label={"Crossover"}
                            options={crossovers}
                            currentValue={engineSettings.crossover.name}
                            keyPropName="id"
                            namePropName="name"
                            onChange={() => console.log("crossover changed")}
                        />
                        <TextField
                            required
                            id="outlined-required"
                            label="CuttingPoints"
                            defaultValue={engineSettings.crossover.cuttingPoints}
                        />
                        {engineSettings.crossover.name==="datetimeOriented"||
                        <DropDown
                            label={"Orientation"}
                            options={orientations}
                            currentValue={engineSettings.crossover.orientation}
                            keyPropName="id"
                            namePropName="name"
                            onChange={() => console.log("orientation changed")}
                        />}
                    </Grid>
                </AccordionDetails>
            </Accordion>
            <Accordion>
                <AccordionSummary
                    expandIcon={<ExpandMoreIcon/>}
                    aria-controls="panel2a-content"
                    id="panel2a-header"
                >
                    <Typography>Mutations</Typography>
                </AccordionSummary>
                <AccordionDetails>
                    <IconButton onClick={() => {
                        console.log("add new mutation")
                    }}>
                        <AddIcon/>
                    </IconButton>
                    <Grid container className={classes.root}>
                        <DropDown
                            label={"Mutation"}
                            options={mutations}
                            currentValue={mutations[0].id}
                            keyPropName="id"
                            namePropName="name"
                            onChange={() => console.log("mutation changed")}
                        />
                        <TextField
                            required
                            id="outlined-required"
                            label="Probability"
                        />
                        <TextField
                            required
                            id="outlined-required"
                            label="totalTuple"
                        />
                        <TextField
                            required
                            id="outlined-required"
                            label="maxTuples"
                        />
                        <DropDown
                            label={"Component"}
                            options={flippingComponent}
                            currentValue={flippingComponent[0].id}
                            keyPropName="id"
                            namePropName="name"
                            onChange={() => console.log("flipping component changed")}
                        />
                    </Grid>
                </AccordionDetails>
            </Accordion>
            <Accordion>
                <AccordionSummary
                    expandIcon={<ExpandMoreIcon/>}
                    aria-controls="panel2a-content"
                    id="panel2a-header"
                >
                    <Typography>End conditions</Typography>
                </AccordionSummary>
                <AccordionDetails>
                    <Grid container className={classes.root} direction={"column"}>
                        <Grid item>
                            <FormControlLabel control={<Checkbox defaultChecked/>} label="Number of generations"/>
                            <TextField
                                required
                                id="outlined-required"
                                label="Number of generations"
                            />
                        </Grid>
                        <Grid item>
                            <FormControlLabel control={<Checkbox defaultChecked/>} label="Fitness score"/>
                            <TextField
                                required
                                id="outlined-required"
                                label="Fitness score"
                            />
                        </Grid>
                        <Grid item>

                            <FormControlLabel control={<Checkbox defaultChecked/>} label="Time"/>
                            <TextField
                                required
                                id="outlined-required"
                                label="Time"
                            />
                        </Grid>
                    </Grid>
                </AccordionDetails>
            </Accordion>
        </Paper>
    )
}
export default EngineSettings;