import Accordion from '@mui/material/Accordion';
import AccordionDetails from '@mui/material/AccordionDetails';
import AccordionSummary from '@mui/material/AccordionSummary';
import AddIcon from '@material-ui/icons/Add';
import {ButtonGroup, IconButton, TextField} from "@material-ui/core";
import DropDown from "../../components/Dropdown";
import ExpandMoreIcon from '@mui/icons-material/ExpandMore';
import Grid from "@material-ui/core/Grid";
import {makeStyles} from "@material-ui/core/styles";
import Paper from "@mui/material/Paper";
import React, {useCallback, useState} from 'react';
import Typography from '@mui/material/Typography';
import EndPredicates from './EndPredicates'
import Button from "@material-ui/core/Button";

const useStyles = makeStyles((theme) => ({
    root: {
        alignItems: "start",
        justifyContent: "space-between",
        padding: 20,
        backgroundColor: "#D3D3D3", //light gray
    },
}));

const selectionTypes = [
    {name: "Truncation", id: "truncation"},
    {name: "Roulette Wheel", id: "rouletteWheel"},
    {name: "Tournament", id: "tournament"},
];
const crossoverTypes = [
    {name: "Daytime Oriented", id: "daytimeOriented"},
    {name: "Aspect Oriented", id: "aspectOriented"},
];
const orientations = [{name: "Teacher", id: "teacher"}, {name: "Class", id: "class"}];
const mutationTypes = [{name: "Flipping", id: "flipping"}, {name: "Sizer", id: "sizer"}];
const flippingComponent = [
    {name: "Hour", id: "H"},
    {name: "Day", id: "D"},
    {name: "Teacher", id: "T"},
    {name: "Class", id: "C"},
    {name: "Subject", id: "S"},
];

const AlgorithmConfiguration = ({algorithmConfiguration,  handleAlgorithmConfigSave, handleCancel}) => {
    const classes = useStyles();
    const [data, setData] = useState(algorithmConfiguration); //currentSettings

    //for first level fields only
    const handleChange = useCallback((e) => {
        setData({
            ...data,
            [e.target.id]: e.target.value,
        })
    }, [data]);

    const handleEndPredicatesChange = useCallback((e, index, propName) => {
        //TODO need to figure out the checkboxes
        const predicateArray = data.engineSettings.mutations;

        const predicate = {
            ...predicateArray[index],
            [propName]: e.target.value,
        };

        const newPredicateArray = [...predicateArray.slice(0, index), predicate, ...predicateArray.slice(index + 1)];

        setData({...data, endPredicates: newPredicateArray});
    }, [data]);

    //selection related
    const renderSelectionExtraField = () => {
        let tempLabel;
        let tempID;

        if (data.engineSettings.selection.name === 'tournament') {
            tempLabel = 'PTE';
            tempID = 'pte';
        } else {
            // if (data.engineSettings.selection.name === 'truncation')
            tempLabel = 'Top Percent';
            tempID = 'topPercent';
        }

        return (<TextField
            required
            id={tempID}
            label={tempLabel}
            defaultValue={data.engineSettings.selection[tempID]}
            onChange={handleSelectionChange}/>)
    };

    const handleSelectionChange = useCallback((e) => {
        const selection = {
            ...data.engineSettings.selection,
            [e.target.id]: e.target.value,
        };

        const engineSettings = {...data.engineSettings, selection};

        setData({...data, engineSettings});
    }, [data]);

    const handleSelectionTypeChange = useCallback((e) => {
        const newSelectionType = selectionTypes.find(a => {
            return a.id === e.target.value;
        })

        const selection = {
            ...data.engineSettings.selection,
            name: newSelectionType.name,
        }

        const engineSettings = {...data.engineSettings, selection};

        setData({...data, engineSettings});
    }, [data]);

    //crossover related
    const handleCrossoverChange = useCallback((e,propName) => {
        const crossover = {
            ...data.engineSettings.crossover,
            [propName]: e.target.value,
        };

        const engineSettings = {...data.engineSettings, crossover};

        setData({...data, engineSettings});
    }, [data]);

    const handleCrossoverTypeChange = useCallback((e) => {
        const newCrossoverType = crossoverTypes.find(a => {
            return a.id === e.target.value;
        })

        const crossover = {
            ...data.engineSettings.crossover,
            name: newCrossoverType.name,
        }

        const engineSettings = {...data.engineSettings, crossover};

        setData({...data, engineSettings});
    }, [data]);

    const handleCrossoverOrientationChange = useCallback((e) => {
        const Orientation = orientations.find(a => {
            return a.id === e.target.value;
        })

        const crossover = {
            ...data.engineSettings.crossover,
            name: Orientation.name,
        }

        const engineSettings = {...data.engineSettings, crossover};

        setData({...data, engineSettings});
    }, [data]);

    //mutation related
    const handleMutationChange = useCallback((e, index, propName) => {
        const mutationsArray = data.engineSettings.mutations;

        const mutation = {
            ...mutationsArray[index],
            [propName]: e.target.value,
        };

        const newMutationsArray = [...mutationsArray.slice(0, index), mutation, ...mutationsArray.slice(index + 1)];
        const engineSettings = {...data.engineSettings, mutations: newMutationsArray};

        setData({...data, engineSettings});
    }, [data]);

    const handleAddMutation = useCallback(() => {
        const mutationsArray = data.engineSettings.mutations;
        const newEmptyMutation = {
            name: mutationTypes[0].id,
        };

        const newMutationsArray = [...mutationsArray, newEmptyMutation];
        const engineSettings = {...data.engineSettings, mutations: newMutationsArray};

        setData({...data, engineSettings});
    }, [data]);

    const renderMutation = (mutation, index) => {
        return (
            <Grid container className={classes.root}>
                <DropDown
                    label={"Mutation"}
                    options={mutationTypes}
                    currentValue={mutation.name}
                    keyPropName="id"
                    namePropName="name"
                    onChange={(e) => handleMutationChange(e, index, 'name')}
                />
                <TextField
                    required
                    label="Probability"
                    defaultValue={mutation.probability}
                    onChange={(e) => handleMutationChange(e, index, 'probability')}
                />
                {renderMutationExtraFields(mutation, index)}
            </Grid>);
    };

    const renderMutationExtraFields = (mutation, index) => {
        if (mutation.name === 'sizer') {
            return (<TextField
                required
                label='Total Tuples'
                defaultValue={mutation.totalTuples}
                onChange={(e) => handleMutationChange(e, index, 'totalTuples')}
            />)
        } else if (mutation.name === 'flipping') {
            return (
                <Grid container className={classes.root}>
                    <TextField
                        required
                        label='Max Tuples'
                        defaultValue={mutation.maxTuples}
                        onChange={(e) => handleMutationChange(e, index, 'maxTuples')}
                    />
                    <DropDown
                        label={'Component'}
                        options={flippingComponent}
                        currentValue={mutation.component}
                        keyPropName='id'
                        namePropName='name'
                        onChange={(e) => handleMutationChange(e, index, 'component')}
                    />
                </Grid>);
        }
    };

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
                            onChange={handleChange}
                        />
                    </Grid>
                </AccordionDetails>
            </Accordion>
            {/*TODO maybe make text fields visible only if the box is ticked*/}
            <Accordion>
                <AccordionSummary
                    expandIcon={<ExpandMoreIcon/>}
                    aria-controls="panel2a-content"
                    id="panel2a-header"
                >
                    <Typography>End Conditions</Typography>
                    {/*    [{"name":"numOfGenerations", "value":300]*/}
                </AccordionSummary>
                <AccordionDetails>
                    <EndPredicates endPredicates={data.endPredicates}
                                   handleEndPredicatesChange={handleEndPredicatesChange}/>
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
                            options={selectionTypes}
                            currentValue={data.engineSettings.selection.name}
                            keyPropName="id"
                            namePropName="name"
                            onChange={handleSelectionTypeChange}
                        />
                        <TextField
                            required
                            id='elitism'
                            label='Elitism'
                            defaultValue={data.engineSettings.selection.elitism}
                            onChange={handleSelectionChange}
                        />
                        {data.engineSettings.selection.name === 'rouletteWheel' || renderSelectionExtraField()}
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
                            options={crossoverTypes}
                            currentValue={data.engineSettings.crossover.name}
                            keyPropName="id"
                            namePropName="name"
                            onChange={(e)=>handleCrossoverChange(e,'name')}
                        />
                        <TextField
                            required
                            label='Cutting Points'
                            defaultValue={data.engineSettings.crossover.numOfCuttingPoints}
                            onChange={(e)=>handleCrossoverChange(e,'numOfCuttingPoints')}
                        />
                        {data.engineSettings.crossover.name === 'daytimeOriented' ||
                        <DropDown
                            label={"Orientation"}
                            options={orientations}
                            currentValue={data.engineSettings.crossover.orientation}
                            keyPropName="id"
                            namePropName="name"
                            onChange={(e)=>handleCrossoverChange(e,'orientation')}
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
                    <IconButton onClick={handleAddMutation}>
                        <AddIcon/>
                    </IconButton>
                    {data.engineSettings.mutations.map(renderMutation)}
                </AccordionDetails>
            </Accordion>

            <ButtonGroup>
                <Button onClick={()=>{
                    console.log({data});
                    handleAlgorithmConfigSave(data)
                }}>Save</Button>
                <Button onClick={handleCancel}>Cancel</Button>
            </ButtonGroup>

        </Paper>
    );
};

export default AlgorithmConfiguration;
