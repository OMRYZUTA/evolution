import React from 'react';
import Typography from "@mui/material/Typography";
import Grid from "@mui/material/Grid";

const RawView= ({quintets})=>{
    return(
        <Grid container direction={"column"} >
            <Typography>
                {`<D,H,C,T,S>`}
            </Typography>
            {quintets.map(quintet=>{
                return(
                    <Grid item>
                        <Typography>
                            {`<${quintet.day},${quintet.hour},${quintet.schoolClassID},${quintet.teacherID}>`}
                        </Typography>
                    </Grid>
                )
            })}
        </Grid>
    )
}
export default RawView;