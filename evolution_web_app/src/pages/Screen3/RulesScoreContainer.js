import Grid from "@mui/material/Grid";
import React from "react";
import Typography from "@mui/material/Typography";

const RulesScoreContainer= ({rulesScores})=>{
// console.log({rulesScores})
    return(
        <Grid container spacing={3}>
            {Object.keys(rulesScores).map(key=>{
                return(
                    <Grid item>
                        <Grid container direction={"column"}>
                            <Grid item>
                                <Typography>
                                    {key}
                                </Typography>
                                <Typography>{(rulesScores[key]).toFixed(2).replace(/[.,]00$/, "")}</Typography>
                            </Grid>
                        </Grid>
                    </Grid>
                )
            })}
        </Grid>
    )
}
export default RulesScoreContainer;