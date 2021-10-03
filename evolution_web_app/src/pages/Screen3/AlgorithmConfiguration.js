import Accordion from '@mui/material/Accordion';
import AccordionDetails from '@mui/material/AccordionDetails';
import AccordionSummary from '@mui/material/AccordionSummary';
import AddIcon from '@mui/icons-material/Add';
import {ButtonGroup, IconButton, TextField} from "@mui/material";
import DropDown from "../../components/Dropdown";
import ExpandMoreIcon from '@mui/icons-material/ExpandMore';
import Grid from "@mui/material/Grid";
import {makeStyles} from "@mui/styles";
import Paper from "@mui/material/Paper";
import React, {useCallback, useState} from 'react';
import Typography from '@mui/material/Typography';
import EndPredicates from './EndPredicates'
import Button from "@mui/material/Button";

const useStyles = makeStyles((theme) => ({
    root: {
        alignItems: "start",
        justifyContent: "space-between",
        padding: 20,
        backgroundColor: "#D3D3D3", //light gray
    },
}));

const selectionTypes = [
    {name: "Truncation", id: "Truncation"},
    {name: "Roulette Wheel", id: "RouletteWheel"},
    {name: "Tournament", id: "Tournament"},
];
const crossoverTypes = [
    {name: "Daytime Oriented", id: "DaytimeOriented"},
    {name: "Aspect Oriented", id: "AspectOriented"},
];
const orientations = [{name: "Teacher", id: "teacher"}, {name: "Class", id: "class"}];
const mutationTypes = [{name: "Flipping", id: "Flipping"}, {name: "Sizer", id: "Sizer"}];
const flippingComponent = [
    {name: "Hour", id: "H"},
    {name: "Day", id: "D"},
    {name: "Teacher", id: "T"},
    {name: "Class", id: "C"},
    {name: "Subject", id: "S"},
];

const AlgorithmConfiguration = ({algorithmConfiguration, handleAlgorithmConfigSave, handleCancel}) => {
    const classes = useStyles();
    const [data, setData] = useState(algorithmConfiguration); //currentSettings

    //for first level fields only
    const handleChange = useCallback((e) => {
        setData({
            ...data,
            [e.target.id]: e.target.value,
        })
    }, [data]);

    const handleEndPredicatesChange = useCallback((endPredicates) => {
        setData({...data, endPredicates});
    }, [data]);

    const handlePopulationSizeChange = useCallback((e, propName) => {
        const engineSettings = {...data.engineSettings, [propName]: e.target.value,};
        setData({...data, engineSettings});
    }, [data]);

    const handleCrossoverChange = useCallback((e, propName) => {
        const crossover = {
            ...data.engineSettings.crossover,
            [propName]: e.target.value,
        };

        const engineSettings = {...data.engineSettings, crossover};

        setData({...data, engineSettings});
    }, [data]);

    //selection related
    const renderSelectionExtraField = () => {
        let tempLabel;
        let tempID;

        if (data.engineSettings.selection.name === 'Tournament') {
            tempLabel = 'PTE';
            tempID = 'pte';
        } else {
            // if (data.engineSettings.selection.name === 'Truncation')
            tempLabel = 'Top Percent';
            tempID = 'topPercent';
        }

        return (<TextField
            required
            id={tempID}
            label={tempLabel}
            defaultValue={data.engineSettings.selection[tempID]}
            onChange={(e) => handleSelectionChange(e, tempID)}/>)
    };

    const handleSelectionChange = useCallback((e, propName) => {
        const selection = {
            ...data.engineSettings.selection,
            [propName]: e.target.value,
        };

        const engineSettings = {...data.engineSettings, selection};

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
            </Grid>
        );
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
                            label='Population size'
                            defaultValue={data.populationSize}
                            onChange={(e) =>
                                handlePopulationSizeChange(e, 'populationSize')}
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
                    id="panel2a-header">
                    <Typography>End Conditions</Typography>
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
                            onChange={(e) => handleSelectionChange(e, 'name')}
                        />
                        <TextField
                            label='Elitism'
                            defaultValue={data.engineSettings.selection.elitism}
                            onChange={(e) => handleSelectionChange(e, 'elitism')}
                        />
                        {data.engineSettings.selection.name === 'RouletteWheel' ? '' : renderSelectionExtraField()}
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
                            onChange={(e) => handleCrossoverChange(e, 'name')}
                        />
                        <TextField
                            required
                            label='Cutting Points'
                            defaultValue={data.engineSettings.crossover.cuttingPoints}
                            onChange={(e) => handleCrossoverChange(e, 'cuttingPoints')}
                        />
                        {data.engineSettings.crossover.name === 'DaytimeOriented' ? '' :
                            <DropDown
                                label={"Orientation"}
                                options={orientations}
                                currentValue={data.engineSettings.crossover.orientation}
                                keyPropName="id"
                                namePropName="name"
                                onChange={(e) => handleCrossoverChange(e, 'orientation')}
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
                <Button onClick={() => {
                    handleAlgorithmConfigSave(data)
                    // TODO notify user Save was successful
                }}>Save</Button>
                <Button onClick={handleCancel}>Cancel</Button>
            </ButtonGroup>

        </Paper>
    );
};

export default AlgorithmConfiguration;
