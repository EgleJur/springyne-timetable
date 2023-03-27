import { useEffect, useState } from "react";
import { Alert, Collapse } from "@mui/material";
import { TextField } from "@mui/material";
import FormControlLabel from "@mui/material/FormControlLabel";
import FormLabel from "@mui/material/FormLabel";
import Checkbox from "@mui/material/Checkbox";
import { DatePicker } from "@mui/x-date-pickers";
import dayjs from "dayjs";
import { apiUrl } from "../../App";

function CreateHolidayPage() {
  const [name, setName] = useState("");
  const [repeats, setRepeats] = useState(false);
  const [nameError, setNameError] = useState("");
  const [startDateError, setStartDateError] = useState("");
  const [endDateError, setEndDateError] = useState("");
  const [success, setSuccess] = useState(false);
  const [failure, setFailure] = useState(false);
  const [startDateValue, setStartDateValue] = useState(null);
  const [endDateValue, setEndDateValue] = useState(null);

  const createNewHoliday = (e) => {
    e.preventDefault();
    setNameError(false);
    setStartDateError(false);
    setEndDateError(false);
    if (
      name === "" ||
      startDateValue === null ||
      endDateValue === null ||
      startDateValue > endDateValue
    ) {
      if (name === "") {
        setNameError(true);
      }
      if (
        startDateValue === null ||
        (startDateValue > endDateValue && endDateValue != null)
      ) {
        setStartDateError(true);
      }
      if (endDateValue === null || endDateValue < startDateValue) {
        setEndDateError(true);
      }
    } else {
      const starts = dayjs(startDateValue).format("YYYY-MM-DD");
      const ends = dayjs(endDateValue).format("YYYY-MM-DD");
      fetch(`${apiUrl}/api/v1/holidays/createHoliday/`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          name,
          starts,
          ends,
          repeats,
        }),
      }).then((result) => {
        if (result.ok) {
          setName("");
          setStartDateValue(null);
          setEndDateValue(null);
          setRepeats(false);
          setSuccess(true);
          setFailure(false);
          setTimeout(() => {
            setSuccess(false);
          }, 5000);
        } else {
          setFailure(true);
          setSuccess(false);
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
          id="create-holiday-name-with-error"
          label="Pavadinimas"
          helperText="Pavadinimas privalomas"
          className="form-control mb-3"
          size="small"
          required
        />

        <DatePicker
          className="mb-3"
          label="Pradžios data"
          value={startDateValue}
          maxDate={endDateValue}
          onChange={(newValue) => {
            setStartDateValue(newValue);
          }}
          renderInput={(params) => (
            <TextField
              fullWidth
              size="small"
              required
              {...params}
              error={!!startDateError}
            />
          )}
        />

        <DatePicker
          className="mb-3"
          label="Pabaigos data"
          // inputFormat="yyyy-MM-dd"
          value={endDateValue}
          minDate={startDateValue}
          onChange={(newValue) => {
            setEndDateValue(newValue);
          }}
          onError={() => setEndDateError(true)}
          renderInput={(params) => (
            <TextField
              fullWidth
              size="small"
              required
              {...params}
              error={!!endDateError}
            />
          )}
        />

        <div className="mb-3">
          <FormLabel id="demo-radio-buttons-group-label"></FormLabel>

          <FormControlLabel
            value="end"
            control={
              <Checkbox
                checked={repeats}
                onChange={handleChange}
                inputProps={{ "aria-label": "controlled" }}
              />
            }
            label="Kartojasi kiekvienais metais"
            labelPlacement="end"
          />
        </div>

        <button
          type="submit"
          className="btn btn-primary"
          onClick={createNewHoliday}
        >
          Pridėti
        </button>
      </form>
    </div>
  );
}

export default CreateHolidayPage;
