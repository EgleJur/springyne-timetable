import { useEffect, useState } from "react";
import { Alert, Collapse } from "@mui/material";
import { TextField } from "@mui/material";
import FormControlLabel from '@mui/material/FormControlLabel';
import FormLabel from '@mui/material/FormLabel';
import Checkbox from '@mui/material/Checkbox';



function CreateHolidayPage() {
  const [name, setName] = useState("");
  const [starts, setStartDate] = useState("");
  const [ends, setEndDate] = useState("");
  const [repeats, setRepeats] = useState(false);
  const [nameError, setNameError] = useState("");
  const [startDateError, setStartDateError] = useState("");
  const [endDateError, setEndDateError] = useState("");
  const [success, setSuccess] = useState(false);
  const [failure, setFailure] = useState(false);
  const [shrink1, setShrink1] = useState(false);

  var moment = require('moment'); // require
  moment().format();

  const createNewRoom = (e) => {
    e.preventDefault();
    setNameError(false);
    setStartDateError(false);
    setEndDateError(false);
    setEndDate();
    if (name === "" || starts === "" || ends === "" || starts > ends) {
      if (name === "") {
        setNameError(true);
      }
      if (starts === "") {
        setStartDateError(true);
      }
      if (ends === "") {
        setEndDateError(true);
      }
      if (starts > ends) {
        setStartDateError(true);
        setEndDateError(true);
        if (starts === "") {
          setEndDateError(false);
        }
        if (ends === "") {
          setStartDateError(false);
        }

      }
    } else {
      fetch("/api/v1/holidays/createHoliday/", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          name,
          starts,
          ends,
          repeats
        }),
      }).then((result) => {
        if (result.ok) {
          setName("");
          setStartDate("");
          setEndDate("");
          setRepeats(false);
          setSuccess(true);
          setFailure(false);
          setShrink1(false);
          setTimeout(() => {
            setSuccess(false);
          }, 5000);
        } else {
          setFailure(true);
          setSuccess(false);
          if (moment(starts, "YYYY-MM-DD").isValid()) {
            setStartDateError(true);
          }
          if (moment(ends, "YYYY-MM-DD").isValid()) {
            setEndDateError(true);
          }
          setTimeout(() => {
            setFailure(false);
          }, 5000);
        }
      });
    }
  };

  const handleChange = (event) => {
    setRepeats(event.target.checked);
  };

  return (
    <div className="mx-3">
      <h2 className="my-5">Pridėti naujas atostogas</h2>
      <Collapse in={success}>
        <Alert
          onClose={() => {
            setSuccess(false);
          }}
          severity="success"
          className="mb-3"
        >
          Įrašas sėkmingai sukurtas
        </Alert>
      </Collapse>

      <Collapse in={failure}>
        <Alert
          onClose={() => {
            setFailure(false);
          }}
          severity="error"
          className="mb-3"
        >
          Įrašo nepavyko sukurti
        </Alert>
      </Collapse>
      <form noValidate>
        <TextField
          error={!!nameError}
          onChange={(e) => setName(e.target.value)}
          value={name}
          id="create-holoday-name-with-error"
          label="Pavadinimas"
          helperText="Pavadinimas privalomas"
          className="form-control mb-3"
          size="small"
          required
        />
        <TextField
          error={!!startDateError}
          onChange={(e) => setStartDate(e.target.value)}
          value={starts}
          id="create-holiday-start-date-with-error"
          label="Atostogų pradžia (MMMM-MM-DD)"
          helperText="Pradžios data privaloma"
          className="form-control mb-3"
          size="small"
          required
        />
        <TextField
          error={!!endDateError}
          onChange={(e) => setEndDate(e.target.value)}
          value={ends}
          id="create-holiday-end-date-with-error"
          label="Atostogų pabaiga (MMMM-MM-DD)"
          helperText="Pabaigos data privaloma"
          className="form-control mb-3"
          size="small"
          required
          onSelect={() => setShrink1(true)}
          InputLabelProps={{ shrink: shrink1 }}
        />

        <div className="mb-3">
          <FormLabel id="demo-radio-buttons-group-label"></FormLabel>

          <FormControlLabel
            value="end"
            control={<Checkbox
              checked={repeats}
              onChange={handleChange}
              inputProps={{ 'aria-label': 'controlled' }}
            />}
            label="Atostogos kartojasi kiekvienais metais"
            labelPlacement="end"
          /></div>


        <button
          type="submit"
          className="btn btn-primary"
          onClick={createNewRoom}
        >
          Pridėti
        </button>
      </form>
    </div>
  );
}

export default CreateHolidayPage;
