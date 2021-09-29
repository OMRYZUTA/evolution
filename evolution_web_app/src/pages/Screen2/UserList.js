import React from 'react';
import {makeStyles} from '@mui/styles';
import ListItem from '@mui/material/ListItem';
import ListItemText from '@mui/material/ListItemText';
import {List, ListItemAvatar} from "@mui/material";
import Avatar from "@mui/material/Avatar";
import PersonOutlinedIcon from '@mui/icons-material/PersonOutlined';

const useStyles = makeStyles((theme) => ({
    root: {
        width: '100%',
        height: 400,
        maxWidth: 300,
        backgroundColor: "#D3D3D3", //light gray
    },
}));


export default function UserList({users}) {
    const classes = useStyles();
    return (
        <div className={classes.root}>
            <List>
                {users.map(user => {
                        return (<ListItem key={user}>
                            <ListItemAvatar>
                                <Avatar>
                                    <PersonOutlinedIcon/>
                                </Avatar>
                            </ListItemAvatar>
                            <ListItemText
                                primary={user}
                            />
                        </ListItem>)
                    }
                )}
            </List>
        </div>
    );
}
