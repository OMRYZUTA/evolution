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
    const [numOfGenerations, setNumOfGenerations] = useState(endPredicates.find(endCondition => endCondition.name === "numOfGenerations" ));
    const [fitnessScore, setFitnessScore] = useState(endPredicates.find(endCondition => endCondition.name === "fitnessScore") );
    const [time, setTime] = useState(endPredicates.find(endCondition => endCondition.name === "time"));
    const [showGeneration,setShowGeneration] =useState(numOfGenerations !==undefined);
    const [showFitness,setShowFitness] =useState(fitnessScore !==undefined);
    const [showTime,setShowTime] =useState(time !==undefined);

    const endPredicatesInitializer = {
        "numOfGenerations": () => {
            setNumOfGenerations({"name": "numOfGenerations", value: ""});
        setShowGeneration(true);
        },
        "fitnessScore": () => {
            setFitnessScore({"name": "fitnessScore", value: ""});
            setShowFitness(true);
            },
        "time": () => {
            setTime({"name": "time", value: ""});
            setShowTime(true);
        }
    }

    const endConditionsDestroyer = {
        "numOfGenerations": () => {
            setNumOfGenerations({name: "numOfGenerations", value: ""});
            setShowGeneration(false);
            },
        "fitnessScore": () => {
            setFitnessScore({name: "numOfGenerations", value: ""});
            setShowFitness(false);
        },
        "time": () => {
            setTime({name: "numOfGenerations", value: ""});
            setShowTime(false);
        }
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
                        checked={showGeneration}
                        onChange={handleCheckBoxChanged}/>
                    <Typography>
                        Num Of Generations
                    </Typography>
                    {showGeneration && <TextField
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
                        checked={showFitness}
                        onChange={handleCheckBoxChanged}/>
                    <Typography> Fitness Score</Typography>
                    {showFitness && <TextField
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
                    <Checkbox checked={showTime}
                              name="time"
                              onChange={handleCheckBoxChanged}/>
                    <Typography>Time</Typography>
                    {showTime && <TextField
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