import React, { useState, useEffect, useContext, useRef } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import api from "../../../services/api.js";

import BookCard from "../../common/book/card.js";
import BookshelfCard from "../../common/bookshelf/card.js";

import overlayContext from "../../context/overlay.js";
import scrollContext from "../../context/scroll.js";
import loggedAccountContext from "../../context/loggedAccount.js";

import "./results.css";

function ResultsList() {
    const { overlay, setOverlay } = useContext(overlayContext);
    const { scroll, setScroll } = useContext(scrollContext);
    const [searchResults, setSearchResults] = useState([]);
    const { setLoggedAccount } = useContext(loggedAccountContext);
    const navigate = useNavigate();
    const { search } = useLocation();
    const moreResults = useRef(true);

    useEffect
        (
            () => {
                let mounted = true;
                const runEffect = async () => {
                    if
                        (
                        !overlay && moreResults.current &&
                        scroll?.target?.scrollHeight - scroll?.target?.scrollTop <= scroll?.target?.offsetHeight + 100
                    ) {
                        await loadResults();
                    }
                }
                runEffect();
                return (() => { mounted = false });
            },
            [scroll, searchResults, search]
        );


    async function loadResults() {
        try {
            setOverlay(true);
            var newSearch = search + "&maxResults=20&startIndex=" + searchResults.length;
            const response = await api.get(
                "/book/advancedsearch"+newSearch,
            );

            if (response.data.length < 20) {
                moreResults.current = false;
            }
            setSearchResults([...searchResults, ...response?.data]);
            setOverlay(false);
        }
        catch (exception) {
            setOverlay(false);
            if (exception?.response?.data === "authentication failed") {
                localStorage.clear();
                setLoggedAccount(null);
            }
            navigate("/");
        }
    }
    
    return (
        <div className="resultsArea">
            {
                searchResults?.map
                (
                    (searchResult, searchResultIndex) => {
                        return (
                            searchResult?.title !== undefined ?
                               <BookCard
                                    key={searchResultIndex}
                                    book={searchResult}
                                    bookIndex={0}
                                    remove={() => {}}
                                    removeable={false}
                                /> : 
                                <BookshelfCard
                                bookshelf={searchResult}
                                key={searchResultIndex}
                            />
                        );
                    }
                )
            }

        </div>
    );
}
export default ResultsList;