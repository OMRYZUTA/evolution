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
import EndPredicates from './EndPredicates'

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
    {name: "Daytime Oriented", id: "daytimeOriented"},
    {name: "Aspect Oriented", id: "aspectOriented"},
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

const AlgorithmConfiguration = ({algorithmConfiguration, handleSave, handleCancel}) => {
    const classes = useStyles();
    const [data, setData] = useState(algorithmConfiguration); //currentSettings

    function renderSelectionExtraField() {
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
    }

    function renderMutationExtraFields(mutation) {
        if (mutation.name === 'sizer') {
            return (<TextField
                required
                id='totalTuples'
                label='Total Tuples'
                defaultValue={mutation.totalTuples}
                onChange={handleMutationChange}
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
            ...data.engineSettings.selection,
            [e.target.id]: e.target.value,
        };

        const engineSettings = {...data.engineSettings, selection};

        setData({...data, engineSettings});
    }, [data]);

    const handleCrossoverChange = useCallback((e) => {
        const crossover = {
            ...data.engineSettings.crossover,
            [e.target.id]: e.target.value,
        };

        const engineSettings = {...data.engineSettings, crossover};

        setData({...data, engineSettings});
    }, [data]);

    const handleMutationChange = useCallback((e) => {
        // const mutation = {
        //     ...data.engineSettings.mutation,
        //     [e.target.id]: e.target.value,
        // };
        //
        // const engineSettings = {...data.engineSettings, mutation};
        //
        // setData({...data, engineSettings});
    }, [data]);

    const handleSelectionTypeChange = useCallback((e) => {
        const newSelectionType = selections.find(a => {
            return a.id === e.target.value;
        })

        const selection = {
            ...data.engineSettings.selection,
            name: newSelectionType.name,
        }

        const engineSettings = {...data.engineSettings, selection};

        setData({...data, engineSettings});
    }, [data]);

    const handleCrossoverTypeChange = useCallback((e) => {
        const newCrossoverType = crossovers.find(a => {
            return a.id === e.target.value;
        })

        const crossover = {
            ...data.engineSettings.crossover,
            name: newCrossoverType.name,
        }

        const engineSettings = {...data.engineSettings, crossover};

        setData({...data, engineSettings});
    }, [data]);

    const handleMutationTypeChange = useCallback((e) => {
        // const newMutationType = mutations.find(a => {
        //     return a.id === e.target.value;
        // })
        //
        // const mutation = {
        //     ...data.engineSettings.mutation,
        //     name: newMutationType.name,
        // }
        //
        // const engineSettings = {...data.engineSettings, mutation};
        //
        // setData({...data, engineSettings});
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

    //TODO mutation is part of list of mutations - need to handle change differenly
    const handleMutationComponentChange = useCallback((e) => {
        // const component = flippingComponent.find(a => {
        //     return a.id === e.target.value;
        // })
        //
        // const mutation = {
        //     ...data.engineSettings.mutation,
        //     name: component.name,
        // }
        //
        // const engineSettings = {...data.engineSettings, mutation};
        //
        // setData({...data, engineSettings});
    }, [data]);

    // const handleContactsChange = (e, new_contact_set) => {
    //     setCurrentApplication({
    //         ...currentApplication,
    //         contact_set: new_contact_set,
    //     });
    // };
    const handleEndPredicatesChange = useCallback(async (e, endPredicates)=>{
        console.log(data);
        await setData(...data,endPredicates);
        console.log(data);
    },[]);

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
                    <EndPredicates endPredicates = {data.endPredicates} handleEndPredicatesChange={handleEndPredicatesChange}/>
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
                            options={crossovers}
                            currentValue={data.engineSettings.crossover.name}
                            keyPropName="id"
                            namePropName="name"
                            onChange={handleCrossoverTypeChange}
                        />
                        <TextField
                            required
                            id='numOfCuttingPoints'
                            label='Cutting Points'
                            defaultValue={data.engineSettings.crossover.numOfCuttingPoints}
                            onChange={handleCrossoverChange}
                        />
                        {data.engineSettings.crossover.name === 'daytimeoriented' ||
                        <DropDown
                            label={"Orientation"}
                            options={orientations}
                            currentValue={data.engineSettings.crossover.orientation}
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
                    {data.engineSettings.mutations.map(mutation => {
                        return (
                            <Grid container className={classes.root}>
                                <DropDown
                                    label={"Mutation"}
                                    options={mutations}
                                    currentValue={mutation.name}
                                    keyPropName="id"
                                    namePropName="name"
                                    onChange={handleMutationTypeChange}
                                />
                                <TextField
                                    required
                                    id='probability'
                                    label="Probability"
                                    defaultValue={mutation.probability}
                                    onChange={handleMutationChange}
                                />
                                {renderMutationExtraFields(mutation)}
                            </Grid>)
                    })}

                </AccordionDetails>
            </Accordion>

        </Paper>
    )
}
export default AlgorithmConfiguration;