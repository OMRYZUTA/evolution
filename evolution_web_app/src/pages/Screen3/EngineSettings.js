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
import React, {useCallback, useState} from 'react';
import Typography from '@mui/material/Typography';

const useStyles = makeStyles((theme) => ({
    root: {
        alignItems: "start",
        justifyContent: "space-between",
        padding: 20,
        backgroundColor: "#D3D3D3", //light gray
    },
}));

const selections = [
    {name: "Truncation", id: "truncation"},
    {name: "Roulette Wheel", id: "rouletteWheel"},
    {name: "Tournament", id: "tournament"},
];
const crossovers = [
    {name: "Daytime Oriented", id: "daytimeoriented"},
    {name: "Aspect Oriented", id: "aspectoriented"},
];
const orientations = [{name: "Teacher", id: "teacher"}, {name: "Class", id: "class"}];
const mutations = [{name: "Flipping", id: "flipping"}, {name: "Sizer", id: "sizer"}];
const flippingComponent = [
    {name: "Hour", id: "H"},
    {name: "Day", id: "D"},
    {name: "Teacher", id: "T"},
    {name: "Class", id: "C"},
    {name: "Subject", id: "S"},
]

const EngineSettings = ({engineSettings, handleSave, handleCancel}) => {
    const classes = useStyles();
    const [data, setData] = useState(engineSettings);

    function renderSelectionExtraField() {
        let tempLabel;
        let tempID;

        if (data.selection.name === 'tournament') {
            tempLabel = 'PTE';
            tempID = 'pte';
        } else {
            // if (data.selection.name === 'truncation')
            tempLabel = 'Top Percent';
            tempID = 'topPercent';
        }

        return (<TextField
            required
            id={tempID}
            label={tempLabel}
            defaultValue={data.selection[tempID]}
            onChange={handleSelectionChange}/>)
    }

    function renderMutationExtraFields(mutation) {
        if (mutation.name === 'sizer') {
            return (<TextField
                required
                id='totalTuples'
                label='Total Tuples'
                defaultValue={mutation.totalTuples}
            />)
        } else if (mutation.name === 'flipping') {
            return (
                <Grid container className={classes.root}>
                    <TextField
                        required
                        id='maxTuples'
                        label='Max Tuples'
                        defaultValue={mutation.maxTuples}
                        onChange={handleMutationChange}
                    />
                    <DropDown
                        label={'Component'}
                        options={flippingComponent}
                        currentValue={mutation.component}
                        keyPropName='id'
                        namePropName='name'
                        onChange={handleMutationComponentChange}
                    />
                </Grid>);
        }
    }

    //for first level fields only
    const handleChange = useCallback((e) => {
        setData({
            ...data,
            [e.target.id]: e.target.value,
        })
    }, [data]);

    const handleSelectionChange = useCallback((e) => {
        const selection = {
            ...data.selection,
            [e.target.id]: e.target.value,
        };

        setData({...data, selection});
    }, [data]);

    const handleCrossoverChange = useCallback((e) => {
        const crossover = {
            ...data.crossover,
            [e.target.id]: e.target.value,
        };

        setData({...data, crossover});
    }, [data]);

    const handleMutationChange = useCallback((e) => {
        const mutation = {
            ...data.mutation,
            [e.target.id]: e.target.value,
        };

        setData({...data, mutation});
    }, [data]);

    const handleSelectionTypeChange = useCallback((e) => {
        const newSelectionType = selections.find(a => {
            return a.id === e.target.value;
        })

        const selection = {
            ...data.selection,
            name: newSelectionType.name,
        }

        setData({...data, selection});
    }, [data]);

    const handleCrossoverTypeChange = useCallback((e) => {
        const newCrossoverType = crossovers.find(a => {
            return a.id === e.target.value;
        })

        const crossover = {
            ...data.crossover,
            name: newCrossoverType.name,
        }

        setData({...data, crossover});
    }, [data]);

    const handleCrossoverOrientationChange = useCallback((e) => {
        const Orientation = orientations.find(a => {
            return a.id === e.target.value;
        })

        const crossover = {
            ...data.crossover,
            name: Orientation.name,
        }

        setData({...data, crossover});
    }, [data]);

    //TODO mutation is part of list of mutations - need to handle change differenly
    const handleMutationComponentChange = useCallback((e) => {
        const component = flippingComponent.find(a => {
            return a.id === e.target.value;
        })

        const mutation = {
            ...data.mutation,
            name: component.name,
        }

        setData({...data, mutation});
    }, [data]);

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
                            id='populationSize'
                            label='Population size'
                            defaultValue={data.populationSize}
                            onChange={handleChange}
                        />
                        <TextField
                            required
                            id='stride'
                            label='Stride'
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
                            <FormControlLabel control={<Checkbox defaultChecked/>} label='Number of Generations'/>
                            <TextField
                                required
                                id='generations'
                                label='Number of Generations'
                            />
                        </Grid>
                        <Grid item>
                            <FormControlLabel control={<Checkbox defaultChecked/>} label="Fitness Score"/>
                            <TextField
                                required
                                id='fitness'
                                label='Fitness Score'
                            />
                        </Grid>
                        <Grid item>
                            <FormControlLabel control={<Checkbox defaultChecked/>} label="Time"/>
                            <TextField
                                required
                                id='time'
                                label='Time'
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
                            onChange={handleSelectionTypeChange}
                        />
                        <TextField
                            required
                            id='elitism'
                            label='Elitism'
                            defaultValue={data.selection.elitism}
                            onChange={handleSelectionChange}
                        />
                        {data.selection.name === 'rouletteWheel' || renderSelectionExtraField()}
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
                            onChange={handleCrossoverTypeChange}
                        />
                        <TextField
                            required
                            id='numOfCuttingPoints'
                            label='Cutting Points'
                            defaultValue={data.crossover.numOfCuttingPoints}
                            onChange={handleCrossoverChange}
                        />
                        {data.crossover.name === 'daytimeoriented' ||
                        <DropDown
                            label={"Orientation"}
                            options={orientations}
                            currentValue={data.crossover.orientation}
                            keyPropName="id"
                            namePropName="name"
                            onChange={handleCrossoverOrientationChange}
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
                                    id='probability'
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