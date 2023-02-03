import { useEffect, useState } from "react";

function HomePage() {

    const [version, setVersion] = useState([]);

    useEffect(() => {
        fetch("/api/v1/timetable/info")
        .then((response) => response.text())
        .then(setVersion);
    },[])

    return ( <div className="ms-5">
        <h1 className="mb-3">Hello</h1>
        <h1 className="mb-3">Hello from Indre</h1>
        <h4>Application version: {version}</h4>
    </div> );
}

export default HomePage;