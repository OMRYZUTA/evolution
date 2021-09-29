import {Checkbox, FormControlLabel, Grid, TextField} from "@mui/material";
import React, {useEffect, useState} from "react";

const CheckedTextBox = ({label, value, handleValueChange}) => {
    const [checked, setChecked] = useState(!!value);

    useEffect(() => {
        if (!checked) {
            handleValueChange(undefined)
        }
    }, [checked]);

    //object and not array, for example: endPredicates: {numOfGenerations: 120} or
    // endPredicates: {numOfGenerations: 120, fitnessScore: 92.3, time: 2}
    return (
        <Grid container>
            <FormControlLabel
                control={<Checkbox checked={checked}
                                   onChange={(e) => {
                                       setChecked(e.target.checked);
                                   }}/>}
                label={label}/>
            <TextField
                disabled={!checked}
                value={value || ''}
                onChange={(e) => {
                    handleValueChange(e.target.value);
                }}/>
        </Grid>
    );
}
export default CheckedTextBox;
