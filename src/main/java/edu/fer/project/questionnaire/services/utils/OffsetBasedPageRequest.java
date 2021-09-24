package edu.fer.project.questionnaire.services.utils;

import java.util.Objects;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class OffsetBasedPageRequest implements Pageable {

  private final int offset;
  private final int limit;
  private final Sort sort;

  private OffsetBasedPageRequest(int offset, int limit, Sort sort) {
    this.offset = offset;
    this.limit = limit;
    this.sort = sort;
  }

  public static OffsetBasedPageRequest of(int offset, int limit, Sort sort) {
    return new OffsetBasedPageRequest(offset, limit, sort);
  }

  public static OffsetBasedPageRequest of(int offset, int limit) {
    return new OffsetBasedPageRequest(offset, limit, Sort.unsorted());
  }

  @Override
  public int getPageNumber() {
    return offset / limit;
  }

  @Override
  public int getPageSize() {
    return limit;
  }

  @Override
  public long getOffset() {
    return offset;
  }

  @Override
  public Sort getSort() {
    return sort;
  }

  @Override
  public Pageable next() {
    return new OffsetBasedPageRequest((int) getOffset() + getPageSize(), getPageSize(), getSort());
  }

  public OffsetBasedPageRequest previous() {
    return hasPrevious()
        ? new OffsetBasedPageRequest((int) getOffset() - getPageSize(), getPageSize(), getSort())
        : this;
  }

  @Override
  public Pageable previousOrFirst() {
    return hasPrevious() ? previous() : first();
  }

  @Override
  public Pageable first() {
    return new OffsetBasedPageRequest(0, getPageSize(), getSort());
  }

  @Override
  public Pageable withPage(int i) {
    return new OffsetBasedPageRequest(i * getPageSize(), getPageSize(), getSort());
  }

  @Override
  public boolean hasPrevious() {
    return offset >= limit;
  }
}
