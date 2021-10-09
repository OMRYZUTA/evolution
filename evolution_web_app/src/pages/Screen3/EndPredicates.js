import {Grid, TextField} from "@mui/material";
import {makeStyles} from "@mui/styles";
import React from "react";
import Typography from "@mui/material/Typography";

const intRegEx = /^\d+$/;
const signedIntRegEx = /^-?\d+$/;
const floatRegEx = /^\d*(\.\d+)?$/;

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
            <Typography>Must set at least 1 End Condition</Typography>
            <Grid item>
                <TextField label="Num Of Generations"
                           value={endPredicates.numOfGenerations || ''}
                           error={endPredicates.numOfGenerationsError}
                           helperText="Whole number"
                           disabled={disableEdit}
                           onChange={(e) => {
                               let value = e.target.value;
                               let error = false;
                               if (!value) {
                                   value = undefined;
                               } else if (intRegEx.test(value)) {
                                   value = parseFloat(value);
                               } else {
                                   error = true;
                               }

                               handleEndPredicatesChange('numOfGenerations', value, error);
                           }}/>
            </Grid>

            <Grid item>
                <TextField label="Fitness Score"
                           value={endPredicates.fitnessScore || ''}
                           error={endPredicates.fitnessScoreError}
                           helperText="Allows decimal point"
                           disabled={disableEdit}
                           onChange={(e) => {
                               let value = e.target.value;
                               let error = false;
                               if (!value) {
                                   value = undefined;
                               } else if (floatRegEx.test(value)) {
                                   value = parseFloat(value);
                               } else {
                                   error = true;
                               }

                               handleEndPredicatesChange('fitnessScore', value, error);
                           }}/>
            </Grid>

            <Grid item>
                <TextField label="Time"
                           value={endPredicates.time || ''}
                           error={endPredicates.timeError}
                           helperText="Whole number"
                           disabled={disableEdit}
                           onChange={(e) => {
                               let value = e.target.value;
                               let error = false;
                               if (!value) {
                                   value = undefined;
                               } else if (intRegEx.test(value)) {
                                   value = parseFloat(value);
                               } else {
                                   error = true;
                               }

                               handleEndPredicatesChange('time', value, error);
                           }}/>
            </Grid>
        </Grid>
    );
}
export default EndPredicates;