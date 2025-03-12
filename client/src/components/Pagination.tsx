import { FC, useEffect, useState } from 'react';

import { range } from '../utils';

const FISRT_PAGE = 1;

// SIDE_COUNT includes first and last page buttons.
// If the selected page leaves the SIDE_COUNT boundaries on the left or right side,
// the number of buttons on each side will be calculated to preserve MAX_CELLS number of buttons.
// Examples for SIDE_COUNT = 3:
// [1] 2 3 4 5 6 10
// 1 48 49 [50] 51 52 100
// 1 95 96 97 98 99 [100]
const SIDE_COUNT = 3;
const MAX_CELLS = 2 * SIDE_COUNT + 1;

type PaginationProps = {
  currentPage: number;
  totalPages: number;
  onPageChange: (page: number) => void;
};

const Pagination: FC<PaginationProps> = ({ currentPage, totalPages, onPageChange }) => {
  const [start, setStart] = useState(1);
  const [end, setEnd] = useState(1);
  const [needFirst, setNeedFirst] = useState(false);
  const [needLast, setNeedLast] = useState(false);

  useEffect(() => {
    const rightDiff = totalPages - currentPage;
    const leftDiff = currentPage - 1;
    const leftCount = Math.max(MAX_CELLS - 2 - rightDiff, Math.min(leftDiff, SIDE_COUNT - 1));
    const rightCount = Math.max(MAX_CELLS - 1 - currentPage, Math.min(rightDiff, SIDE_COUNT - 1));
    const newStart = Math.max(currentPage - leftCount, 1);
    const widthCount = leftCount + rightCount + 1;
    const newEnd = Math.min(newStart + widthCount, totalPages);
    setStart(newStart);
    setEnd(newEnd);
  }, [currentPage, totalPages]);

  useEffect(() => {
    setNeedFirst(start > 1);
    setNeedLast(end <= totalPages);
  }, [start, end, totalPages]);

  const handlePrev = () => {
    if (currentPage > 1) {
      onPageChange(currentPage - 1);
    }
  };

  const handleNext = () => {
    if (currentPage < totalPages) {
      onPageChange(currentPage + 1);
    }
  };

  const handlePageClick = (page: number) => {
    onPageChange(page);
  };

  return (
    <div className="flex justify-center mt-4">
      <nav aria-label="Page navigation">
        <ul className="flex list-none border border-gray-300 rounded-lg overflow-hidden">
          <li>
            <button
              className={`px-3 py-2 border-r border-gray-300 ${
                currentPage === 1 ? 'text-gray-400 cursor-not-allowed' : 'hover:bg-gray-200'
              }`}
              onClick={handlePrev}
              disabled={currentPage === 1}
            >
              &laquo;
            </button>
          </li>

          {needFirst && (
            <li>
              <button
                className={`px-3 py-2 border-r border-gray-300 ${
                  currentPage === FISRT_PAGE ? 'bg-blue-500 text-white' : 'hover:bg-gray-200'
                }`}
                onClick={() => handlePageClick(FISRT_PAGE)}
              >
                {FISRT_PAGE}
              </button>
            </li>
          )}

          {range(start, end).map((page) => (
            <li key={page}>
              <button
                className={`px-3 py-2 border-r border-gray-300 ${
                  currentPage === page ? 'bg-blue-500 text-white' : 'hover:bg-gray-200'
                }`}
                onClick={() => handlePageClick(page)}
              >
                {page}
              </button>
            </li>
          ))}

          {needLast && (
            <li>
              <button
                className={`px-3 py-2 border-r border-gray-300 ${
                  currentPage === totalPages ? 'bg-blue-500 text-white' : 'hover:bg-gray-200'
                }`}
                onClick={() => handlePageClick(totalPages)}
              >
                {totalPages}
              </button>
            </li>
          )}

          <li>
            <button
              className={`px-3 py-2 ${
                currentPage === totalPages
                  ? 'text-gray-400 cursor-not-allowed'
                  : 'hover:bg-gray-200'
              }`}
              onClick={handleNext}
              disabled={currentPage === totalPages}
            >
              &raquo;
            </button>
          </li>
        </ul>
      </nav>
    </div>
  );
};

export default Pagination;
