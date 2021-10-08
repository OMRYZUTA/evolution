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
import React, {useCallback, useEffect, useState} from 'react';
import Typography from '@mui/material/Typography';
import EndPredicates from './EndPredicates'
import Button from "@mui/material/Button";

const intRegEx = /^\d+$/;
const floatRegEx = /^\d*(\.\d+)?$/;
const selectionTypes = [
    {name: "Truncation", type: "Truncation"},
    {name: "Roulette Wheel", type: "RouletteWheel"},
    {name: "Tournament", type: "Tournament"},
];
const crossoverTypes = [
    {name: "Daytime Oriented", type: "DayTimeOriented"},
    {name: "Aspect Oriented", type: "AspectOriented"},
];
const orientations = [{name: "Teacher", type: "teacher"}, {name: "Class", type: "class"}];
const mutationTypes = [{name: "Flipping", type: "Flipping"}, {name: "Sizer", type: "Sizer"}];
const flippingComponent = [
    {name: "Hour", type: "H"},
    {name: "Day", type: "D"},
    {name: "Teacher", type: "T"},
    {name: "Class", type: "C"},
    {name: "Subject", type: "S"},
];
const useStyles = makeStyles((theme) => ({
    root: {
        alignItems: "start",
        justifyContent: "space-between",
        padding: 20,
        backgroundColor: "#D3D3D3", //light gray
    },
}));

const AlgorithmConfiguration = ({algorithmConfiguration, handleAlgorithmConfigSave}) => {
    const classes = useStyles();
    const [data, setData] = useState(algorithmConfiguration); //currentSettings

    //#region useState() for flags indicating incorrect type in field (for helpText)
    const [strideError, setStrideError] = useState(false);
    const [populationSizeError, setPopulationSizeError] = useState(false);
    const [elitismError, setElitismError] = useState(false);
    const [pteError, setPteError] = useState(false);
    const [topPercentError, setTopPercentError] = useState(false);
    const [cuttingPointsError, setCuttingPointsError] = useState(false);
    const [probabilityError, setProbabilityError] = useState(false);
    const [maxTuplesError, setMaxTuplesError] = useState(false);
    const [totalTuplesError, setTotalTuplesError] = useState(false);
    //#endregion

    useEffect(() => {
        setData(algorithmConfiguration);
    }, [algorithmConfiguration])

    const handleStrideChange = useCallback((e) => {
        const value = e.target.value.trim();

        if (intRegEx.test(value)) {
            setStrideError(false);
            setData({
                ...data,
                stride: parseInt(value, 10),
            });
        } else {
            setStrideError(true);
        }
    }, [data]);

    const handlePopulationSizeChange = useCallback((e) => {
        const value = e.target.value.trim();

        if (intRegEx.test(value)) {
            setPopulationSizeError(false);
            const engineSettings = {...data.engineSettings, populationSize: parseInt(value, 10)};
            setData({...data, engineSettings});
        } else {
            setPopulationSizeError(true);
        }
    }, [data]);

    const handleEndPredicatesChange = useCallback((endPredicates) => {
        setData({...data, endPredicates});
    }, [data]);

    //#region selection related
    const setValueInSelection = useCallback((propName, value) => {
        const selection = {
            ...data.engineSettings.selection,
            [propName]: value,
        };

        const engineSettings = {...data.engineSettings, selection};

        setData({...data, engineSettings});
    }, [data]);

    const handleElitismChange = useCallback((e) => {
        const value = e.target.value.trim() || '0'; // if after trim we have an empty string - use '0'
        if (intRegEx.test(value)) {
            setElitismError(false);
            setValueInSelection('elitism', parseInt(value, 10));
        } else {
            setElitismError(true);
        }
    }, [data]);

    const handlePTEChange = useCallback((e) => {
        const value = e.target.value.trim();

        if (floatRegEx.test(value)) {
            setPteError(false);
            setValueInSelection('pte', parseFloat(value));
        } else {
            setPteError(true);
        }
    }, [data]);

    const handleTopPercentChange = useCallback((e) => {
        const value = e.target.value.trim();
        if (intRegEx.test(value)) {
            setTopPercentError(false);
            setValueInSelection('topPercent', parseInt(value, 10));
        } else {
            setTopPercentError(true);
        }
    }, [data]);

    const renderSelectionExtraField = () => {
        if (data.engineSettings.selection.type === 'Tournament') {
            return (
                <TextField
                    required
                    error={pteError}
                    helperText={pteError ? 'Invalid value (must be a number)' : ''}
                    label="PTE"
                    value={data.engineSettings.selection.pte || ''}
                    onChange={handlePTEChange}/>
            );
        } else {
            // if (data.engineSettings.selection.type === 'Truncation')
            return (
                <TextField
                    required
                    error={topPercentError}
                    helperText={topPercentError ? 'Invalid value (must be a number)' : ''}
                    label="Top Percent"
                    value={data.engineSettings.selection.topPercent || ''}
                    onChange={handleTopPercentChange}/>
            );
        }
    };
    //#endregion

    //#region crossover related
    const handleCuttingPointsChange = useCallback((e) => {
        const value = e.target.value.trim();
        if (intRegEx.test(value)) {
            setCuttingPointsError(false);
            setValueInCrossover('cuttingPoints', parseInt(value, 10));
        } else {
            setCuttingPointsError(true);
        }
    }, [data]);

    const setValueInCrossover = useCallback((propName, value) => {
        const crossover = {
            ...data.engineSettings.crossover,
            [propName]: value,
        };

        const engineSettings = {...data.engineSettings, crossover};

        setData({...data, engineSettings});
    }, [data]);
    //#endregion

    //#region mutation related
    const handleAddMutation = useCallback(() => {
        const mutationsArray = data.engineSettings.mutations;
        const newEmptyMutation = {
            type: mutationTypes[0].type,
        };

        const newMutationsArray = [...mutationsArray, newEmptyMutation];
        const engineSettings = {...data.engineSettings, mutations: newMutationsArray};

        setData({...data, engineSettings});
    }, [data]);

    const setValueInMutation = useCallback((propName, value, index) => {
        const mutationsArray = data.engineSettings.mutations;

        const mutation = {
            ...mutationsArray[index],
            [propName]: value,
        };

        const newMutationsArray = [...mutationsArray.slice(0, index), mutation, ...mutationsArray.slice(index + 1)];
        const engineSettings = {...data.engineSettings, mutations: newMutationsArray};

        setData({...data, engineSettings});
    }, [data]);

    const handleProbabilityChange = useCallback((e, index) => {
        const value = e.target.value.trim();

        if (floatRegEx.test(value)) {
            setProbabilityError(false);
            setValueInMutation('probability', parseFloat(value), index);
        } else {
            setProbabilityError(true);
        }
    }, [data]);

    const handleTotalTuplesChange = useCallback((e, index) => {
        const value = e.target.value.trim();

        if (intRegEx.test(value)) {
            setTotalTuplesError(false);
            setValueInMutation('totalTuples', parseInt(value, 10), index);
        } else {
            setTotalTuplesError(true);
        }
    }, [data]);

    const handleMaxTuplesChange = useCallback((e, index) => {
        const value = e.target.value.trim();

        if (intRegEx.test(value)) {
            setMaxTuplesError(false);
            setValueInMutation('maxTuples', parseInt(value, 10), index);
        } else {
            setMaxTuplesError(true);
        }
    }, [data]);

    const renderMutation = (mutation, index) => {
        return (
            <Grid container className={classes.root}>
                <DropDown
                    label={'Mutation'}
                    options={mutationTypes}
                    currentValue={mutation.type || ''}
                    keyPropName='type'
                    namePropName='name'
                    onChange={(e) => setValueInMutation('type', e.target.value, index)}/>
                <TextField
                    required
                    label='Probability'
                    error={probabilityError}
                    helperText={probabilityError ? 'Invalid value (must be a number)' : ''}
                    value={mutation.probability || ''}
                    onChange={(e) => handleProbabilityChange(e, index)}/>
                {renderMutationExtraFields(mutation, index)}
            </Grid>
        );
    };

    const renderMutationExtraFields = (mutation, index) => {
        if (mutation.type === 'Sizer') {
            return (
                <TextField
                    required
                    error={totalTuplesError}
                    helperText={totalTuplesError ? 'Invalid value (must be a number)' : ''}
                    label='Total Tuples'
                    value={mutation.totalTuples || ''}
                    onChange={(e) => handleTotalTuplesChange(e, index)}/>
            )
        } else if (mutation.type === 'Flipping') {
            return (
                <Grid container className={classes.root}>
                    <TextField
                        required
                        error={maxTuplesError}
                        helperText={maxTuplesError ? 'Invalid value (must be a number)' : ''}
                        label='Max Tuples'
                        value={mutation.maxTuples}
                        onChange={(e) => handleMaxTuplesChange(e, index)}/>
                    <DropDown
                        label={'Component'}
                        options={flippingComponent}
                        currentValue={mutation.component || ''}
                        keyPropName='type'
                        namePropName='name'
                        onChange={(e) => setValueInMutation('component', e.target.value, index)}/>
                </Grid>
            );
        }
    };
    //#endregion

    //#region render accordions
    const renderGeneralDetails = () => {
        return (
            <Accordion>
                <AccordionSummary
                    expandIcon={<ExpandMoreIcon/>}>
                    <Typography>Population and Stride</Typography>
                </AccordionSummary>
                <AccordionDetails>
                    <Grid container className={classes.root}>
                        <TextField
                            required
                            error={populationSizeError}
                            helperText={populationSizeError ? 'Invalid value (must be a number)' : ''}
                            label="Population size"
                            value={data.engineSettings.populationSize || ''}
                            onChange={handlePopulationSizeChange}/>
                        <TextField
                            required
                            error={strideError}
                            helperText={strideError ? 'Invalid value (must be a number)' : ''}
                            label='Stride'
                            value={data.stride || ''}
                            onChange={handleStrideChange}/>
                    </Grid>
                </AccordionDetails>
            </Accordion>
        );
    };

    const renderEndConditions = () => {
        return (
            <Accordion>
                <AccordionSummary
                    expandIcon={<ExpandMoreIcon/>}>
                    <Typography>End Conditions</Typography>
                </AccordionSummary>
                <AccordionDetails>
                    <EndPredicates endPredicates={data.endPredicates}
                                   handleEndPredicatesChange={handleEndPredicatesChange}/>
                </AccordionDetails>
            </Accordion>
        );
    };

    const renderSelection = () => {
        return (
            <Accordion>
                <AccordionSummary
                    expandIcon={<ExpandMoreIcon/>}>
                    <Typography>Selection</Typography>
                </AccordionSummary>
                <AccordionDetails>
                    <Grid container className={classes.root}>
                        <DropDown
                            label={"Selection"}
                            options={selectionTypes}
                            currentValue={data.engineSettings.selection.type || ''}
                            keyPropName="type"
                            namePropName="name"
                            onChange={(e) => setValueInSelection('type', e.target.value)}/>
                        <TextField
                            error={elitismError}
                            helperText={elitismError ? 'Invalid value (must be a number)' : ''}
                            label="Elitism"
                            value={data.engineSettings.selection.elitism || ''}
                            onChange={handleElitismChange}/>
                        {data.engineSettings.selection.type === 'RouletteWheel' ? '' : renderSelectionExtraField()}
                    </Grid>
                </AccordionDetails>
            </Accordion>
        );
    };

    const renderCrossover = () => {
        return (
            <Accordion>
                <AccordionSummary
                    expandIcon={<ExpandMoreIcon/>}>
                    <Typography>Crossover</Typography>
                </AccordionSummary>
                <AccordionDetails>
                    <Grid container className={classes.root}>
                        <DropDown
                            label={"Crossover"}
                            options={crossoverTypes}
                            currentValue={data.engineSettings.crossover.type || ''}
                            keyPropName="type"
                            namePropName="name"
                            onChange={(e) => setValueInCrossover('type', e.target.value)}/>
                        <TextField
                            required
                            error={cuttingPointsError}
                            helperText={cuttingPointsError ? 'Invalid value (must be a number)' : ''}
                            label='Cutting Points'
                            value={data.engineSettings.crossover.cuttingPoints || ''}
                            onChange={handleCuttingPointsChange}/>
                        {data.engineSettings.crossover.type === 'DayTimeOriented' ? '' :
                            <DropDown
                                label={"Orientation"}
                                options={orientations}
                                currentValue={data.engineSettings.crossover.orientation || ''}
                                keyPropName="type"
                                namePropName="name"
                                onChange={(e) => setValueInCrossover('orientation', e.target.value)}/>}
                    </Grid>
                </AccordionDetails>
            </Accordion>
        );
    };

    const renderMutations = () => {
        return (
            <Accordion>
                <AccordionSummary
                    expandIcon={<ExpandMoreIcon/>}>
                    <Typography>Mutations</Typography>
                </AccordionSummary>
                <AccordionDetails>
                    <IconButton onClick={handleAddMutation}>
                        <AddIcon/>
                    </IconButton>
                    {data.engineSettings.mutations.map(renderMutation)}
                </AccordionDetails>
            </Accordion>
        );
    };
    //#endregion

    const noError = !strideError && !populationSizeError && !elitismError && !pteError && !topPercentError
        && !cuttingPointsError && !probabilityError && !maxTuplesError && !totalTuplesError;
    const saveEnabled = (data !== algorithmConfiguration) && noError;

    return (
        <Paper>
            {renderGeneralDetails()}
            {renderEndConditions()}
            {renderSelection()}
            {renderCrossover()}
            {renderMutations()}
            <ButtonGroup>
                <Button disabled={!saveEnabled}
                        onClick={() => {
                            handleAlgorithmConfigSave(data);
                        }}>Save</Button>
                <Button onClick={() => {
                    setData(algorithmConfiguration);
                }}>Cancel</Button>
            </ButtonGroup>
        </Paper>
    );
};

export default AlgorithmConfiguration;
