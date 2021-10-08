import {TextField} from "@mui/material";
import React from "react";

const CheckedTextBox = ({label, value, handleValueChange, valueError}) => {
    //object and not array, for example: endPredicates: {numOfGenerations: 120} or
    // endPredicates: {numOfGenerations: 120, fitnessScore: 92.3, time: 2}
    return (
        <TextField
            // disabled={!checked}
            label={label}
            error={valueError}
            helperText={valueError ? 'Invalid value (must be a number)' : ''}
            value={value || ''}
            onChange={(e) => {
                handleValueChange(e.target.value);
            }}/>
    );
}
export default CheckedTextBox;
