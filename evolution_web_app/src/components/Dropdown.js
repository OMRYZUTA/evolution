import React from 'react';
import InputLabel from '@mui/material/InputLabel';
import MenuItem from '@mui/material/MenuItem';
import FormControl from '@mui/material/FormControl';
import Select from '@mui/material/Select';
import Box from "@mui/material/Box";

export default function DropDown({
                                     label,
                                     options,      // an array of {key, value} pairs
                                     currentValue, // a single key value, must be a valid option
                                     keyPropName,  // the name of the "key" property from the pair
                                     namePropName, // the name of the "name" property from the pair
                                     onChange,     // callback function that notifies "outside" that the user selected a different option
                                 }) {
    const tempOptions = options.sort((s1, s2) => (s1[namePropName]).localeCompare(s2[namePropName]))

    const extendedOptions = [{
        [keyPropName]: '',
        [namePropName]: '',
    }, ...tempOptions];

    return (
        <Box sx={{minWidth: 120}}>
            <FormControl fullWidth>
                <InputLabel>{label}</InputLabel>
                <Select
                    label={label}
                    value={currentValue}
                    onChange={onChange}>
                    {extendedOptions.map((option) => <MenuItem key={option[keyPropName]}
                                                               value={option[keyPropName]}>{option[namePropName]}</MenuItem>)}
                </Select>
            </FormControl>
        </Box>
    );
}
