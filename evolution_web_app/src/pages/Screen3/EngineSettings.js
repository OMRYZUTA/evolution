import Accordion from '@mui/material/Accordion';
import AccordionDetails from '@mui/material/AccordionDetails';
import AccordionSummary from '@mui/material/AccordionSummary';
import AddIcon from '@material-ui/icons/Add';
import {Checkbox, FormControlLabel, IconButton, TextField} from "@material-ui/core";
import DropDown from "../../components/Dropdown";
import ExpandMoreIcon from '@mui/icons-material/ExpandMore';
import Grid from "@material-ui/core/Grid";
import {makeStyles} from "@material-ui/core/styles";
import Paper from "@mui/material/Paper";
import React, {useState} from 'react';
import Typography from '@mui/material/Typography';

const useStyles = makeStyles((theme) => ({
    root: {
        alignItems: "start",
        justifyContent: "space-between",
        padding: 20,
        backgroundColor: "#D3D3D3", //light gray
    },
}));

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

const EngineSettings = ({engineSettings, handleSave, handleCancel}) => {
    const classes = useStyles();
    const [data, setData] = useState(engineSettings);

    function renderSelectionExtraField() {
        let tempLabel;
        let tempValue;

        if (data.selection.name === "tournament") {
            tempLabel = "PTE";
            tempValue = data.selection.pte;
        } else {
            // if (data.selection.name === "truncation")
            tempLabel = "Top Percent";
            tempValue = data.selection.topPercent;
        }

        return (<TextField required id="outlined-required" label={tempLabel} defaultValue={tempValue}/>)
    }

    function renderMutationExtraFields(mutation) {
        if (mutation.name === "sizer") {
            return (<TextField
                required
                id="outlined-required"
                label="Total Tuples"
                defaultValue={mutation.totalTuples}
            />)
        } else if (mutation.name === "flipping") {
            return (
                <Grid container className={classes.root}>
                    <TextField
                        required
                        id="outlined-required"
                        label="Max Tuples"
                        defaultValue={mutation.maxTuples}
                    />
                    <DropDown
                        label={"Component"}
                        options={flippingComponent}
                        currentValue={mutation.component}
                        keyPropName="id"
                        namePropName="name"
                        onChange={() => console.log("flipping component changed")}
                    />
                </Grid>);
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
                    <Typography>Population and Stride</Typography>
                </AccordionSummary>
                <AccordionDetails>
                    <Grid container className={classes.root}>
                        <TextField
                            required
                            id="outlined-required"
                            label="Population size"
                            defaultValue={data.populationSize}
                        />
                        <TextField
                            required
                            id="outlined-required"
                            label="Stride"
                            defaultValue={data.stride}
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
                    <Typography>End Conditions</Typography>
                </AccordionSummary>
                <AccordionDetails>
                    <Grid container className={classes.root} direction={"column"}>
                        <Grid item>
                            <FormControlLabel control={<Checkbox defaultChecked/>} label="Number of Generations"/>
                            <TextField
                                required
                                id="outlined-required"
                                label="Number of Generations"
                            />
                        </Grid>
                        <Grid item>
                            <FormControlLabel control={<Checkbox defaultChecked/>} label="Fitness Score"/>
                            <TextField
                                required
                                id="outlined-required"
                                label="Fitness Score"
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
                            currentValue={data.selection.name}
                            keyPropName="id"
                            namePropName="name"
                            onChange={() => console.log("selection changed")}
                        />
                        <TextField
                            required
                            id="outlined-required"
                            label="Elitism"
                            defaultValue={data.selection.elitism}
                        />
                        {data.selection.name === "rouletteWheel" || renderSelectionExtraField()}
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
                            currentValue={data.crossover.name}
                            keyPropName="id"
                            namePropName="name"
                            onChange={() => console.log("crossover changed")}
                        />
                        <TextField
                            required
                            id="outlined-required"
                            label="Cutting Points"
                            defaultValue={data.crossover.numOfCuttingPoints}
                        />
                        {data.crossover.name === "daytimeoriented" ||
                        <DropDown
                            label={"Orientation"}
                            options={orientations}
                            currentValue={data.crossover.orientation}
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
                    {data.mutations.map(mutation => {
                        return (
                            <Grid container className={classes.root}>
                                <DropDown
                                    label={"Mutation"}
                                    options={mutations}
                                    currentValue={mutation.name}
                                    keyPropName="id"
                                    namePropName="name"
                                    onChange={() => console.log("mutation changed")}
                                />
                                <TextField
                                    required
                                    id="outlined-required"
                                    label="Probability"
                                    defaultValue={mutation.probability}
                                />
                                {renderMutationExtraFields(mutation)}
                            </Grid>)
                    })}

                </AccordionDetails>
            </Accordion>

        </Paper>
    )
}
export default EngineSettings;