import React from "react";

import { Typography } from "@mui/material";

const MuiTypography: React.FC = () => {
  return (
    <div>
      <Typography variant="h1">h1 heading</Typography>
      <Typography variant="h2">h2 heading</Typography>
      <Typography variant="h3">h3 heading</Typography>
      <Typography variant="h4">h4 heading</Typography>
      <Typography variant="h5">h5 heading</Typography>
      <Typography variant="h6">h6 heading</Typography>

      <Typography variant="subtitle1">subtitle1</Typography>
      <Typography variant="subtitle2">subtitle2</Typography>

      <Typography variant="body1">
        Lorem ipsum dolor sit amet consectetur adipisicing elit. Quisquam
        voluptatum, quod, quia, voluptas quae voluptates quibusdam voluptatibus
        quos quas quidem nemo. Quisquam, quae. Quisquam, quae. Quisquam, quae.
      </Typography>
      
      <Typography variant="body2">
        Lorem ipsum dolor sit amet consectetur adipisicing elit. Quisquam
        voluptatum, quod, quia, voluptas quae voluptates quibusdam voluptatibus
        quos quas quidem nemo. Quisquam, quae. Quisquam, quae. Quisquam, quae.
      </Typography>
    </div>
  );
};

export default MuiTypography;
