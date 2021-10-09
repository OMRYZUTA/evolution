import CheckedTextBox from './CheckedTextBox'
import {Grid} from "@mui/material";
import {makeStyles} from "@mui/styles";
import React from "react";

const useStyles = makeStyles(() => ({
    root: {
        alignItems: "start",
        justifyContent: "space-between",
        padding: 20,
        backgroundColor: "#D3D3D3", //light gray
    },
}));

const EndPredicates = ({endPredicates, handleEndPredicatesChange, disableEdit}) => {
    const classes = useStyles();

    //object and not array, for example: endPredicates: {numOfGenerations: 120} or
    // endPredicates: {numOfGenerations: 120, fitnessScore: 92.3, time: 2}
    return (
        <Grid container className={classes.root} direction={"column"} spacing={1}>
            <Grid item>
                <CheckedTextBox label="Num Of Generations"
                                value={endPredicates.numOfGenerations}
                                handleValueChange={(value) => {
                                    handleEndPredicatesChange('numOfGenerations', value)
                                }}
                                valueError={endPredicates.numOfGenerationsError}
                                disableEdit={disableEdit}/>
            </Grid>

            <Grid item>
                <CheckedTextBox label="Fitness Score"
                                value={endPredicates.fitnessScore}
                                handleValueChange={(value) => {
                                    handleEndPredicatesChange('fitnessScore', value);
                                }}
                                valueError={endPredicates.fitnessScoreError}/>
            </Grid>

            <Grid item>
                <CheckedTextBox label="Time"
                                value={endPredicates.time}
                                handleValueChange={(value) => {
                                    handleEndPredicatesChange('time', value);
                                }}
                                valueError={endPredicates.timeError}/>
            </Grid>
        </Grid>
    );
}
export default EndPredicates;