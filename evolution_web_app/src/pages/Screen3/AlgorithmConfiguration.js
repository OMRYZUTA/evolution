import Accordion from '@mui/material/Accordion';
import AccordionDetails from '@mui/material/AccordionDetails';
import AccordionSummary from '@mui/material/AccordionSummary';
import AddIcon from '@mui/icons-material/Add';
import DeleteIcon from '@mui/icons-material/Delete';
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
const signedIntRegEx = /^-?\d+$/;
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
const orientations = [{name: "Teacher", type: "TEACHER"}, {name: "Class", type: "CLASS"}];
const mutationTypes = [{name: "Flipping", type: "Flipping"}, {name: "Sizer", type: "Sizer"}];
const flippingComponent = [
    {name: "Class", type: "C"},
    {name: "Day", type: "D"},
    {name: "Hour", type: "H"},
    {name: "Subject", type: "S"},
    {name: "Teacher", type: "T"},
];
const useStyles = makeStyles((theme) => ({
    root: {
        alignItems: "start",
        justifyContent: "space-between",
        padding: 20,
        backgroundColor: "#D3D3D3", //light gray
    },
}));

function nullCoalesce(value, defValue = '') {
    return value == null ? '' : value;
}

const AlgorithmConfiguration = ({
                                    algorithmConfiguration,
                                    handleAlgorithmConfigSave,
                                    handleAlgorithmConfigChanged,
                                    disableEdit,
                                }) => {
    const classes = useStyles();
    const [data, setData] = useState(algorithmConfiguration); //currentSettings

    //#region useState() for flags indicating incorrect type in field (for helpText)
    const [strideError, setStrideError] = useState(false);
    const [populationSizeError, setPopulationSizeError] = useState(false);
    const [elitismError, setElitismError] = useState(false);
    const [pteError, setPteError] = useState(false);
    const [topPercentError, setTopPercentError] = useState(false);
    const [cuttingPointsError, setCuttingPointsError] = useState(false);
    //#endregion

    // when the algorithm configuration changes from the server - we want to update the local state
    useEffect(() => {
        setData(algorithmConfiguration);
    }, [algorithmConfiguration])

    useEffect(() => {
        handleAlgorithmConfigChanged(data);
    }, [data]);

    const handleStrideChange = useCallback((e) => {
        let value = e.target.value.trim();

        if (intRegEx.test(value)) {
            setStrideError(false);
            value = parseInt(value, 10);
        } else {
            setStrideError(true);
        }

        setData({
            ...data,
            stride: value,
        });
    }, [data]);

    const handlePopulationSizeChange = useCallback((e) => {
        let value = e.target.value.trim();

        if (intRegEx.test(value)) {
            setPopulationSizeError(false);
            value = parseInt(value, 10);
        } else {
            setPopulationSizeError(true);
        }

        const engineSettings = {...data.engineSettings, populationSize: value};
        setData({...data, engineSettings});
    }, [data]);

    const handleEndPredicatesChange = useCallback((predicateName, value, error) => {
        const endPredicates = {
            ...data.endPredicates,
            [predicateName]: value,
            [predicateName + 'Error']: error,
        }
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
        let value = e.target.value.trim() || '0'; // if after trim we have an empty string use '0'

        if (intRegEx.test(value)) {
            setElitismError(false);
            value = parseInt(value, 10);
        } else {
            setElitismError(true);
        }

        setValueInSelection('elitism', value);

    }, [data]);

    const handlePTEChange = useCallback((e) => {
        let value = e.target.value.trim();

        if (floatRegEx.test(value)) {
            setPteError(false);
            value = parseFloat(value) || 0;
        } else {
            setPteError(true);
        }

        setValueInSelection('pte', value);
    }, [data]);

    const handleTopPercentChange = useCallback((e) => {
        let value = e.target.value.trim();

        if (intRegEx.test(value)) {
            setTopPercentError(false);
            value = parseInt(value, 10);
        } else {
            setTopPercentError(true);
        }

        setValueInSelection('topPercent', value);
    }, [data]);

    const renderSelectionExtraField = () => {
        if (data.engineSettings.selection.type === 'Tournament') {
            return (
                <TextField
                    required
                    error={pteError}
                    helperText="decimal point number (0-1)"
                    disabled={disableEdit}
                    label="PTE"
                    value={nullCoalesce(data.engineSettings.selection.pte)}
                    onChange={handlePTEChange}/>
            );
        } else {
            // if (data.engineSettings.selection.type === 'Truncation')
            return (
                <TextField
                    required
                    error={topPercentError}
                    helperText="whole number"
                    disabled={disableEdit}
                    label="Top Percent"
                    value={nullCoalesce(data.engineSettings.selection.topPercent)}
                    onChange={handleTopPercentChange}/>
            );
        }
    };
    //#endregion

    //#region crossover related
    const handleCuttingPointsChange = useCallback((e) => {
        let value = e.target.value.trim();
        if (intRegEx.test(value)) {
            setCuttingPointsError(false);
            value = parseInt(value, 10);
        } else {
            setCuttingPointsError(true);
        }

        setValueInCrossover('cuttingPoints', value);
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
            component: flippingComponent[0].type,
        };

        const newMutationsArray = [...mutationsArray, newEmptyMutation];
        const engineSettings = {...data.engineSettings, mutations: newMutationsArray};

        setData({...data, engineSettings});
    }, [data]);

    const handleRemoveMutation = useCallback((index) => {
        const mutationsArray = data.engineSettings.mutations;
        const newMutationsArray = [...mutationsArray.slice(0, index), ...mutationsArray.slice(index + 1)];
        const engineSettings = {...data.engineSettings, mutations: newMutationsArray};

        setData({...data, engineSettings});
    }, [data])

    const setValueInMutation = useCallback((index, propName, value, error) => {
        const mutationsArray = data.engineSettings.mutations;
        const mutation = {
            ...mutationsArray[index],
            [propName]: value,
            [propName + 'Error']: error,
        };

        const newMutationsArray = [...mutationsArray.slice(0, index), mutation, ...mutationsArray.slice(index + 1)];
        const engineSettings = {...data.engineSettings, mutations: newMutationsArray};

        setData({...data, engineSettings});
    }, [data]);

    const handleProbabilityChange = useCallback((e, index) => {
        let value = e.target.value.trim();
        let error = false;

        if (floatRegEx.test(value)) {
            value = parseFloat(value) || 0;
        } else {
            error = true;
        }

        setValueInMutation(index, 'probability', value, error);
    }, [data]);

    const handleTotalTuplesChange = useCallback((e, index) => {
        let value = e.target.value.trim();
        let error = false;

        if (signedIntRegEx.test(value)) {
            value = parseInt(value, 10);
        } else {
            error = true;
        }

        setValueInMutation(index, 'totalTuples', value, error);
    }, [data]);

    const handleMaxTuplesChange = useCallback((e, index) => {
        let value = e.target.value.trim();
        let error = false;

        if (intRegEx.test(value)) {
            value = parseInt(value, 10);
        } else {
            error = true;
        }

        setValueInMutation(index, 'maxTuples', value, error);
    }, [data]);

    const renderMutation = (mutation, index) => {
        return (
            <Grid container item sx={{padding: "10px 0px"}} direction={"column"}>
                <Grid item sx={{padding: "10px 0px"}}>
                    <Grid container direction={"row"}>
                        <Grid item>
                            <DropDown
                                label={'Mutation'}
                                options={mutationTypes}
                                currentValue={nullCoalesce(mutation.type)}
                                keyPropName='type'
                                namePropName='name'
                                disableEdit={disableEdit}
                                onChange={(e) => setValueInMutation(index, 'type', e.target.value)}/>
                        </Grid>
                        <Grid item>
                            <TextField
                                required
                                error={mutation.probabilityError}
                        helperText="decimal point number (0-1)"
                                disabled={disableEdit}
                                label='Probability'
                                value={nullCoalesce(mutation.probability)}
                                onChange={(e) => handleProbabilityChange(e, index)}/>
                        </Grid>
                    </Grid>
                </Grid>
                <Grid item>
                    <Grid container>
                        {renderMutationExtraFields(mutation, index)}
                <IconButton onClick={() => {
                    handleRemoveMutation(index);
                }}>
                    <DeleteIcon/>
                </IconButton>
                    </Grid>
                </Grid>
            </Grid>
        );
    };

    const renderMutationExtraFields = (mutation, index) => {
        if (mutation.type === 'Sizer') {
            return (
                <Grid item>
                    <TextField
                        required
                        error={mutation.totalTuplesError}
                        helperText="whole number"
                        disabled={disableEdit}
                        label='Total Tuples'
                        value={nullCoalesce(mutation.totalTuples)}
                        onChange={(e) => handleTotalTuplesChange(e, index)}/>
                </Grid>
            )
        } else if (mutation.type === 'Flipping') {
            return [
                <Grid item>
                    <TextField
                        required
                        error={mutation.maxTuplesError}
                        helperText="whole number"
                        disabled={disableEdit}
                        label='Max Tuples'
                        value={mutation.maxTuples}
                        onChange={(e) => handleMaxTuplesChange(e, index)}/>
                </Grid>,
                <Grid item>
                    <DropDown
                        label={'Component'}
                        options={flippingComponent}
                        currentValue={nullCoalesce(mutation.component)}
                        keyPropName='type'
                        namePropName='name'
                        disableEdit={disableEdit}
                        onChange={(e) => setValueInMutation(index, 'component', e.target.value)}/>
                </Grid>,
            ];
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
                            helperText="whole number"
                            disabled={disableEdit}
                            label="Population size"
                            value={nullCoalesce(data.engineSettings.populationSize)}
                            onChange={handlePopulationSizeChange}/>
                        <TextField
                            required
                            error={strideError}
                            helperText="whole number"
                            disabled={disableEdit}
                            label='Stride'
                            value={nullCoalesce(data.stride)}
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
                                   handleEndPredicatesChange={handleEndPredicatesChange}
                                   disableEdit={disableEdit}/>
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
                            currentValue={nullCoalesce(data.engineSettings.selection.type)}
                            keyPropName="type"
                            namePropName="name"
                            disableEdit={disableEdit}
                            onChange={(e) => setValueInSelection('type', e.target.value)}/>
                        <TextField
                            error={elitismError}
                            helperText="whole number"
                            disabled={disableEdit}
                            label="Elitism"
                            value={nullCoalesce(data.engineSettings.selection.elitism)}
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
                            currentValue={nullCoalesce(data.engineSettings.crossover.type)}
                            keyPropName="type"
                            namePropName="name"
                            disableEdit={disableEdit}
                            onChange={(e) => setValueInCrossover('type', e.target.value)}/>
                        <TextField
                            required
                            error={cuttingPointsError}
                            helperText="whole number"
                            disabled={disableEdit}
                            label='Cutting Points'
                            value={nullCoalesce(data.engineSettings.crossover.cuttingPoints)}
                            onChange={handleCuttingPointsChange}/>
                        {data.engineSettings.crossover.type === 'DayTimeOriented' ? '' :
                            <DropDown
                                label={"Orientation"}
                                options={orientations}
                                currentValue={nullCoalesce(data.engineSettings.crossover.orientation)}
                                keyPropName="type"
                                namePropName="name"
                                disableEdit={disableEdit}
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
                    <Grid container direction={"column"} className={classes.root}>
                        {data.engineSettings.mutations.map(renderMutation)}
                    </Grid>
                </AccordionDetails>
            </Accordion>
        );
    };
    //#endregion

    const selectionError = elitismError || pteError || topPercentError;
    const endPredicatesError = data.endPredicates.numOfGenerationsError || data.endPredicates.fitnessScoreError || data.endPredicates.timeError;
    const mutationsError = data.engineSettings.mutations.some((mutation) => mutation.probabilityError || mutation.totalTuplesError || mutation.maxTuplesError);
    const noError = !strideError
        && !populationSizeError
        && !endPredicatesError
        && !selectionError
        && !cuttingPointsError
        && !mutationsError;
    const saveEnabled = (data !== algorithmConfiguration) && noError;
    const cancelEnabled = data !== algorithmConfiguration;

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
                        }}>Apply</Button>
                <Button disabled={!cancelEnabled}
                        onClick={() => {
                            setData(algorithmConfiguration);
                        }}>Cancel</Button>
            </ButtonGroup>
        </Paper>
    );
};

export default AlgorithmConfiguration;
