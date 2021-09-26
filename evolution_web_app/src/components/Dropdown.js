import React from 'react';
import { makeStyles } from '@material-ui/core/styles';
import InputLabel from '@material-ui/core/InputLabel';
import MenuItem from '@material-ui/core/MenuItem';
import FormControl from '@material-ui/core/FormControl';
import Select from '@material-ui/core/Select';

const useStyles = makeStyles((theme) => ({
    formControl: {
        margin: theme.spacing(0),
        minWidth: 120,
    },
    selectEmpty: {
        marginTop: theme.spacing(0),
    },
}));

const EMPTY_OPTION = {
    id: undefined,
    name: '',
};

export default function DropDown({
                                     label,
                                     options,      // an array of {key, value} pairs
                                     currentValue, // a single key value, must be a valid option
                                     keyPropName,  // the name of the "key" property from the pair
                                     namePropName, // the name of the "name" property from the pair
                                     onChange,     // callback function that notifies "outside" that the user selected a different option
                                 }) {
    const classes = useStyles();
    let tempOptions = options.sort((s1, s2) => (s1.name).localeCompare(s2.name))

    const extendedOptions = [EMPTY_OPTION, ...tempOptions];

    return (
        <FormControl className={classes.formControl}>
            <InputLabel>{label}</InputLabel>
            <Select
                value={currentValue}
                onChange={onChange}
            >
                {extendedOptions.map((option) => <MenuItem key={option[keyPropName]} value={option[keyPropName]}>{option[namePropName]}</MenuItem>)}
            </Select>
        </FormControl>
    );
}
