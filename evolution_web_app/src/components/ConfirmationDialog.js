import React from 'react';
import Button from '@mui/material/Button';
import DialogTitle from '@mui/material/DialogTitle';
import DialogContent from '@mui/material/DialogContent';
import DialogActions from '@mui/material/DialogActions';
import Dialog from '@mui/material/Dialog';
import {DialogContentText} from "@mui/material";

export default function ConfirmationDialog({handleOk, handleCancel}) {
    return (
        <Dialog open={true}
                onClose={handleCancel}>
            <DialogTitle id="alert-dialog-title">
                Are you sure?
            </DialogTitle>
            <DialogContent>
                <DialogContentText id="alert-dialog-description">
                    If you continue, your data from the previous run will be lost
                </DialogContentText>
            </DialogContent>
            <DialogActions>
                <Button onClick={handleCancel}>Cancel</Button>
                <Button onClick={handleOk} autoFocus>
                    Continue
                </Button>
            </DialogActions>
        </Dialog>
    );
}
