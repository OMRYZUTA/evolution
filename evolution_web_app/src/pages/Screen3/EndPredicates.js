import CheckedTextBox from './CheckedTextBox'
import {Grid} from "@mui/material";
import {makeStyles} from "@mui/styles";
import React, {useState} from "react";

const useStyles = makeStyles(() => ({
    root: {
        alignItems: "start",
        justifyContent: "space-between",
        padding: 20,
        backgroundColor: "#D3D3D3", //light gray
    },
}));

const EndPredicates = ({endPredicates, handleEndPredicatesChange}) => {
    const floatRegEx = /^\d*(\.\d+)?$/;
    const [valueError, setValueError] = useState(false);
    const classes = useStyles();

    const handleTextChanged = (predicateName, value) => {
        if (floatRegEx.test(value)) {
            setValueError(false);
            handleEndPredicatesChange({...endPredicates, [predicateName]: parseFloat(value)});
        } else {
            setValueError(true);
        }
    };

    //object and not array, for example: endPredicates: {numOfGenerations: 120} or
    // endPredicates: {numOfGenerations: 120, fitnessScore: 92.3, time: 2}
    return (
        <Grid container className={classes.root} direction={"column"}>
            <Grid item>
                <CheckedTextBox label="Num Of Generations"
                                value={endPredicates.numOfGenerations}
                                handleValueChange={(value) => {
                                    handleTextChanged('numOfGenerations', value)
                                }}
                                valueError={valueError}/>
            </Grid>

            <Grid item>
                <CheckedTextBox label="Fitness Score"
                                value={endPredicates.fitnessScore}
                                handleValueChange={(value) => {
                                    handleTextChanged('fitnessScore', value);
                                }}
                                valueError={valueError}/>
            </Grid>

            <Grid item>
                <CheckedTextBox label="Time"
                                value={endPredicates.time}
                                handleValueChange={(value) => {
                                    handleTextChanged('time', value);
                                }}
                                valueError={valueError}/>
            </Grid>
        </Grid>
    );
}
export default EndPredicates;