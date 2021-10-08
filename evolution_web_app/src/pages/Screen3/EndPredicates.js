import CheckedTextBox from './CheckedTextBox'
import {Grid} from "@mui/material";
import {makeStyles} from "@mui/styles";
import React, {useCallback, useState} from "react";

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
    const [valueError, setValueError] = useState({});
    const classes = useStyles();

    const handleTextChanged = useCallback((predicateName, value) => {
        if (!value) {
            setError(predicateName, false);
            handleEndPredicatesChange({...endPredicates, [predicateName]: undefined});
        } else if (floatRegEx.test(value)) {
            setError(predicateName, false);
            handleEndPredicatesChange({...endPredicates, [predicateName]: parseFloat(value)});
        } else {
            setError(predicateName, true);
            handleEndPredicatesChange({...endPredicates, [predicateName]: value});
        }
    }, [endPredicates, handleEndPredicatesChange]);

    const setError = (predicateName, error) => {
        setValueError({
            ...valueError,
            [predicateName]: error,
        });
    }

    //object and not array, for example: endPredicates: {numOfGenerations: 120} or
    // endPredicates: {numOfGenerations: 120, fitnessScore: 92.3, time: 2}
    return (
        <Grid container className={classes.root} direction={"column"} spacing={1}>
            <Grid item>
                <CheckedTextBox label="Num Of Generations"
                                value={endPredicates.numOfGenerations}
                                handleValueChange={(value) => {
                                    handleTextChanged('numOfGenerations', value)
                                }}
                                valueError={valueError.numOfGenerations}/>
            </Grid>

            <Grid item>
                <CheckedTextBox label="Fitness Score"
                                value={endPredicates.fitnessScore}
                                handleValueChange={(value) => {
                                    handleTextChanged('fitnessScore', value);
                                }}
                                valueError={valueError.fitnessScore}/>
            </Grid>

            <Grid item>
                <CheckedTextBox label="Time"
                                value={endPredicates.time}
                                handleValueChange={(value) => {
                                    handleTextChanged('time', value);
                                }}
                                valueError={valueError.time}/>
            </Grid>
        </Grid>
    );
}
export default EndPredicates;