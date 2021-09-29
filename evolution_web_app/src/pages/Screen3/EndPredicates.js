import {Checkbox, TextField} from "@material-ui/core";
import Grid from "@material-ui/core/Grid";
import {makeStyles} from "@material-ui/core/styles";
import React, {useCallback, useState} from "react";
import {Typography} from "@mui/material";

const useStyles = makeStyles((theme) => ({
    root: {
        alignItems: "start",
        justifyContent: "space-between",
        padding: 20,
        backgroundColor: "#D3D3D3", //light gray
    },
}));

const EndPredicates = ({endPredicates, handleEndPredicatesChange}) => {
    const classes = useStyles();
    const [numOfGenerations, setNumOfGenerations] = useState(endPredicates.find(endCondition => endCondition.name === "numOfGenerations") || false);
    const [fitnessScore, setFitnessScore] = useState(endPredicates.find(endCondition => endCondition.name === "fitnessScore") || false);
    const [time, setTime] = useState(endPredicates.find(endCondition => endCondition.name === "time") || false);

    const endPredicatesInitializer = {
        //TODO remove hardcoded later
        "numOfGenerations": () => setNumOfGenerations({"name": "numOfGenerations", value: "100"}),
        "fitnessScore": () => setFitnessScore({"name": "fitnessScore", value: "90"}),
        "time": () => setTime({"name": "time", value: "5"})
    }

    const endConditionsDestroyer = {
        "numOfGenerations": () => setNumOfGenerations({name: "numOfGenerations", value: ""}),
        "fitnessScore": () => setFitnessScore({name: "numOfGenerations", value: ""}),
        "time": () => setTime({name: "numOfGenerations", value: ""})
    }

    const handleCheckBoxChanged = useCallback((e) => {
        if (e.target.checked) {
            endPredicatesInitializer[e.target.name]();
        } else {
            endConditionsDestroyer[e.target.name]();
        }
        // handleEndPredicatesChange(e,buildEndPredicatesResult());
    },[]);

    const buildEndPredicatesResult = useCallback(() => {
        const endPredicates = [numOfGenerations, fitnessScore, time].filter(endPredicate => endPredicate !== undefined);
        console.log(endPredicates);
        return endPredicates;
    },[]);

    return (
        <Grid container className={classes.root} direction={"column"}>
            <Grid item>
                <Grid container>
                    <Checkbox
                        name='numOfGenerations'
                        checked={numOfGenerations.value!==""}
                        onChange={handleCheckBoxChanged}/>
                    <Typography>
                        Num Of Generations
                    </Typography>
                    {numOfGenerations.value && <TextField
                        required
                        name='numOfGenerations'
                        label='Number of Generations'
                        onChange={() => {
                            console.log("end conds list onchange")
                        }}
                        defaultValue={numOfGenerations.value}
                    />}
                </Grid>
            </Grid>
            <Grid item>
                <Grid container>
                    <Checkbox
                        name={"fitnessScore"}
                        checked={fitnessScore.value!==""}
                        onChange={handleCheckBoxChanged}/>
                    <Typography> Fitness Score</Typography>
                    {fitnessScore.value && <TextField
                        required
                        id='fitness'
                        label='Fitness Score'
                        onChange={() => {
                            console.log("end conds list onchange")
                        }}
                        defaultValue={fitnessScore.value}
                    />}
                </Grid>
            </Grid>
            <Grid item>
                <Grid container spacing={2}>
                    <Checkbox checked={time.value!==""}
                              name="time"
                              onChange={handleCheckBoxChanged}/>
                    <Typography>Time</Typography>
                    {time.value && <TextField
                        required
                        name='time'
                        label='Time'
                        onChange={() => {
                            console.log("end conds list onchange")
                        }}
                        defaultValue={time.value}
                    />}
                </Grid>
            </Grid>
        </Grid>);
}
export default EndPredicates;