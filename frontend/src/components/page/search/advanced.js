import React, { useState, useContext } from "react";
import searchContext from "../../context/search.js";

import "./advanced.css";

function AdvancedSearch() {

    //const [searchParams, setSearchParams] = useState([]);
    const { search, setSearch } = useContext(searchContext);
    console.log("context" + "" + searchContext);
    const [searchParams, setSearchParams] = useState({
        term: "",
        title: "",
        author: "",
        subject: "",
        publisher: "",
        isbn: "",
        maxResults: 10,
        startIndex: 0,
        resultType: "",
        bookshelfName: "", 
        ownerName: "",
    });

    const handleSearch = async () => {
        console.log(searchParams);
        setSearch(searchParams);
        console.log("context" + "" + searchContext);
    }

    return (
        <div>

            <select
                value={searchParams.resultType}
                onChange={(e) => setSearchParams({ ...searchParams, resultType: e.target.value })}
            >
                <option value="book">Book</option>
                <option value="bookshelf">Bookshelf</option>
            </select>

            <div className="bookSearchArea">
                <div className="label">Term:</div>
                <input
                    type="text"
                    placeholder="Search term"
                    value={searchParams.term}
                    onChange={(e) => setSearchParams({ ...searchParams, term: e.target.value })}
                    spellCheck={false}
                />
                <div className="label">Title:</div>
                <input
                    type="title"
                    placeholder="Search title"
                    value={searchParams.title}
                    onChange={(e) => setSearchParams({ ...searchParams, title: e.target.value })}
                    spellCheck={false}
                />
                <div className="label">Author:</div>
                <input
                    type="author"
                    placeholder="Search author"
                    value={searchParams.author}
                    onChange={(e) => setSearchParams({ ...searchParams, author: e.target.value })}
                    spellCheck={false}
                />
                <div className="label">Subject:</div>
                <input
                    type="subject"
                    placeholder="Search subject"
                    value={searchParams.subject}
                    onChange={(e) => setSearchParams({ ...searchParams, subject: e.target.value })}
                    spellCheck={false}
                />
                <div className="label">Publisher:</div>
                <input
                    type="publisher"
                    placeholder="Search publisher"
                    value={searchParams.publisher}
                    onChange={(e) => setSearchParams({ ...searchParams, publisher: e.target.value })}
                    spellCheck={false}
                />
                <div className="label">Isbn:</div>
                <input
                    type="isbn"
                    placeholder="Search isbn"
                    value={searchParams.isbn}
                    onChange={(e) => setSearchParams({ ...searchParams, isbn: e.target.value })}
                    spellCheck={false}
                />
            </div>

            <button onClick={handleSearch}>Search</button>
        </div>
    );
}

export default AdvancedSearch;





